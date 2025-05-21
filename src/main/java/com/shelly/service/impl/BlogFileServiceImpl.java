package com.shelly.service.impl;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shelly.common.ServiceException;

import com.shelly.entity.pojo.BlogFile;
import com.shelly.entity.vo.res.FileResp;
import com.shelly.entity.vo.req.FolderReq;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.FileQuery;
import com.shelly.service.BlogFileService;
import com.shelly.mapper.BlogFileMapper;
import com.shelly.strategy.context.UploadStrategyContext;
import com.shelly.utils.FileUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
* @author Shelly6
* @ description 针对表【t_blog_file】的数据库操作Service实现
* @ createDate 2024-07-22 20:24:46
*/
@Service
@Slf4j
@RequiredArgsConstructor
public class BlogFileServiceImpl extends ServiceImpl<BlogFileMapper, BlogFile>
    implements BlogFileService{

    private final BlogFileMapper blogFileMapper;

    @Value("${upload.local.path}")
    private String localPath;

    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    @Autowired
    private HttpServletResponse response;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createFolder(FolderReq folder) {
        String fileName = folder.getFileName();
        String filePath = folder.getFilePath();
        // 判断目录是否存在
        BlogFile blogFile = blogFileMapper.selectOne(new LambdaQueryWrapper<BlogFile>()
                .select(BlogFile::getId)
                .eq(BlogFile::getFilePath, filePath)
                .eq(BlogFile::getFileName, fileName));
        if(blogFile != null){
            throw new IllegalArgumentException("该目录已存在");
        }
        //在本地创建目录
        File directory = new File(localPath + filePath + "/" + fileName);
        if (FileUtils.mkdir(directory)) {
            BlogFile newBlogFile = BlogFile.builder()
                    .fileName(fileName)
                    .filePath(filePath)
                    .isDir(1)
                    .build();
            blogFileMapper.insert(newBlogFile);
        } else {
            throw new ServiceException("创建目录失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Integer> fileIdList) {
        List<BlogFile> blogFiles = blogFileMapper.selectList(new LambdaQueryWrapper<BlogFile>()
                .select(BlogFile::getFileName, BlogFile::getFilePath, BlogFile::getExtendName, BlogFile::getIsDir)
                .in(BlogFile::getId, fileIdList));
        blogFileMapper.deleteBatchIds(fileIdList);
        blogFiles.forEach(blogFile -> {
            File file ;
            String fileName = localPath + blogFile.getFilePath() + "/" + blogFile.getFileName();
            //两步操作，删除数据库和本地文件
            if (blogFile.getIsDir().equals(1)) {
                //如果是文件夹，就删除文件夹下的所有文件
                //数据库保存的路径不含前缀，所以不使用localPath，这里的path是单层的，eg:/pic
                String filePath = blogFile.getFilePath() + blogFile.getFileName();
                blogFileMapper.delete(new LambdaQueryWrapper<BlogFile>().eq(BlogFile::getFilePath, filePath));
                //删除文件夹
                file = new File(fileName);
                if (file.exists()) {
                    //递归删除
                    FileUtils.deleteFile(file);
                }
            } else {
                //如果是文件，就直接删除
                file = new File(fileName + "." + blogFile.getExtendName());
                if (file.exists()) {
                    file.delete();
                }
            }
        });
    }

    @Override
    public PageResult<FileResp> listFile(FileQuery fileQuery) {
        Page<BlogFile> page = new Page<>(fileQuery.getOrigPage(), fileQuery.getSize());
        LambdaQueryWrapper<BlogFile> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(fileQuery.getFilePath())) {
            wrapper.eq(BlogFile::getFilePath, fileQuery.getFilePath());
        }
        IPage<BlogFile> blogFileIPage = blogFileMapper.selectPage(page, wrapper);
        if (blogFileIPage.getTotal() == 0){
            return new PageResult<>();
        }
        // 将查询结果转换为 DTO
        List<FileResp> friendBackDTOList = blogFileIPage.getRecords().stream()
                .map(file -> {
                    FileResp dto = new FileResp();
                    BeanUtils.copyProperties(file, dto);
                    return dto;
                })
                .toList();
        // 返回分页结果
        return new PageResult<>(friendBackDTOList, blogFileIPage.getTotal());
    }

    @Override
    public void uploadFile(MultipartFile file, String filePath) {
        String uploadPath = "/".equals(filePath) ? filePath : filePath + "/";
        // 上传文件返回url
        String url = uploadStrategyContext.executeUploadStrategy(file, uploadPath);
        //操作数据库
        saveBlogFile(file, url, filePath);
    }

    @Override
    public void downloadFile(Integer fileId) {
        // 查询文件信息
        BlogFile blogFile = blogFileMapper.selectOne(new LambdaQueryWrapper<BlogFile>()
                .select(BlogFile::getFilePath, BlogFile::getFileName,
                        BlogFile::getExtendName, BlogFile::getIsDir)
                .eq(BlogFile::getId, fileId));
        Assert.notNull(blogFile, "文件不存在");
                       //举例:usr/ +  /images  +  /
        String filePath = localPath + blogFile.getFilePath() + "/";
        // 要下载的不是目录
        if (blogFile.getIsDir().equals(0)) {
            //举例:57875rt81882v  +   .jpg
            String fileName = blogFile.getFileName() + "." + blogFile.getExtendName();
            downloadFile(filePath, fileName);
        } else {
            // 下载的是目录则压缩下载
            //  举例:/talk
            String fileName = filePath + blogFile.getFileName();
            File src = new File(fileName);
            File dest = new File(fileName + ".zip");
            try {
                ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(dest));
                // 压缩文件
                toZip(src, zipOutputStream, src.getName());
                zipOutputStream.close();
                // 下载压缩包
                downloadFile(filePath, blogFile.getFileName() + ".zip");
            } catch (IOException e) {
                log.error("downloadFile fail, {}", e.getMessage());
            } finally {
                if (dest.exists()) {
                    dest.delete();
                }
            }
        }
    }
    private void downloadFile(String filePath, String fileName) {
        FileInputStream fileInputStream = null;
        ServletOutputStream outputStream = null;
        try {
            // 设置文件名
            response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            fileInputStream = new FileInputStream(filePath + fileName);
            outputStream = response.getOutputStream();
            IOUtils.copyLarge(fileInputStream, outputStream);
        } catch (IOException e) {
            throw new ServiceException("文件下载失败");
        } finally {
            IOUtils.closeQuietly(fileInputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }
    private static void toZip(File src, ZipOutputStream zipOutputStream, String name) throws IOException {
        for (File file : Objects.requireNonNull(src.listFiles())) {
            if (file.isFile()) {
                // 判断文件，变成ZipEntry对象，放入到压缩包中
                ZipEntry zipEntry = new ZipEntry(name + "/" + file.getName());
                // 读取文件中的数据，写到压缩包
                zipOutputStream.putNextEntry(zipEntry);
                FileInputStream fileInputStream = new FileInputStream(file);
                int b;
                while ((b = fileInputStream.read()) != -1) {
                    zipOutputStream.write(b);
                }
                fileInputStream.close();
                zipOutputStream.closeEntry();
            } else {
                toZip(file, zipOutputStream, name + "/" + file.getName());
            }
        }
    }

    public void saveBlogFile(MultipartFile file, String url, String filePath) {
        try {
            // 获取文件md5值
            String md5 = FileUtils.getMd5(file.getInputStream());
            // 获取文件扩展名
            String extName = FileUtils.getExtension(file);
            BlogFile existFile = blogFileMapper.selectOne(new LambdaQueryWrapper<BlogFile>()
                    .select(BlogFile::getId)
                    .eq(BlogFile::getFileName, md5)
                    .eq(BlogFile::getFilePath, filePath));
            if (Objects.nonNull(existFile)) {
                return;
            }
            // 保存文件信息
            BlogFile newFile = BlogFile.builder()
                    .fileUrl(url)
                    .fileName(md5)
                    .filePath(filePath)
                    .extendName(extName)
                    .fileSize((int) file.getSize())
                    .isDir(0)
                    .build();
            blogFileMapper.insert(newFile);
        } catch (IOException e) {
            log.error("saveBlogFile is error, {}", e.getMessage());
        }
    }
}




