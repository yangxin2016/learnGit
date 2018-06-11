package com.security.wrr.controller;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.security.wrr.encode.demo.DemoForCpabe;
import com.security.wrr.model.*;
import com.security.wrr.repository.CpabeRepository;
import com.security.wrr.repository.LogRepository;
import com.security.wrr.repository.MyDownloadRepository;
import com.security.wrr.repository.UserRepository;
import com.security.wrr.util.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Controller
@RequestMapping("/cpabe")
public class CpabeController {

    Logger logger = Logger.getLogger(CpabeController.class);
    private static Integer pageSize=10;

    @Autowired
    CpabeRepository cpabeRepository;
    @Autowired
    MyDownloadRepository myDownloadRepository;
    @Autowired
    LogRepository logRepository;

    @Value("${SERVER_FILEPATH}")
    String serverFilePath;
    /**
     * 查询
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getFileInfoList")
    @ResponseBody
    public Object getFileInfoList(HttpServletRequest request, HttpServletResponse response){
        Integer page = Integer.parseInt(request.getParameter("page"));
        String keyword = request.getParameter("keyword");
        String sortWay = request.getParameter("sort");
        String position = request.getParameter("position");
        String userName = request.getParameter("userName");
        logger.info("------搜索关键词："+keyword+"----页数："+page);
        PageRequest pageable = new PageRequest(page-1,pageSize,sortWay.equals("asc")?ASC:DESC,"uploadTime");
        //判断关键词是否存在于文档的keywords字段中
      //  Page<FileInfo> files = cpabeRepository.findByKeywordsContains(keyword,pageable);
        Page<FileInfo> files = null;
        List<String> keywords = new ArrayList<String>();
        //逻辑与操作,用”&&“分割，表明同时包含
        if(keyword.contains("&&")){
            String[] keywordsArr = keyword.split("&&");
            for(String str:keywordsArr){
                if(str.trim().length()>0){
                    //转成md5码再搜索
                    keywords.add(MD5Util.getMd5CodeByStr(str));
                }
            }

            files = cpabeRepository.andSearch(keywords,position.split(" "),pageable);
        }else{//逻辑或操作用空格分割，表明可能包含
            String[] keywordsArr = keyword.split(" ");
            for(String str:keywordsArr){
                if(str.trim().length()>0){
                    //转成md5码再搜索
                    keywords.add(MD5Util.getMd5CodeByStr(str));
                }
            }
            files = cpabeRepository.orSearch(keywords,position.split(" "),pageable);
        }
        logger.info("搜索成功：总共"+ files.getTotalElements());

        FileListResult fileListResult = new FileListResult();
        fileListResult.setFileInfoList(files.getContent());
        fileListResult.setTotalCount(files.getTotalElements());
        fileListResult.setTotalPage(files.getTotalPages());

        //记录搜索历史
        Log log = new Log();
        log.setUser(userName);
        log.setOperate("search");
        log.setKeyword(keyword);
        log.setCreateTime(new Date());
        logRepository.save(log);
        return fileListResult;

    }

    /**
     * 获取热门下载列表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getHotDownloadList")
    @ResponseBody
    public Object getHotDownloadList(HttpServletRequest request, HttpServletResponse response){
        PageRequest pageable = new PageRequest(0,pageSize,DESC,"downloadTimes");

        Page<FileInfo> pages= cpabeRepository.findAll(pageable);

        return pages.getContent();
    }

    /**
     * 获取你可能喜欢列表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getYouMaybeLike")
    @ResponseBody
    public Object getYouMaybeLike(HttpServletRequest request, HttpServletResponse response){
        PageRequest pageable = new PageRequest(0,pageSize,DESC,"downloadTimes");

        Page<FileInfo> pages= cpabeRepository.findAll(pageable);

        return pages.getContent();
    }

    /**
     * 接收上传文件的信息和内容
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/fileUpload")
    @ResponseBody
    public String saveFileInfo(HttpServletRequest request, HttpServletResponse response,ModelMap model){
        //允许跨域
        response.addHeader("Access-Control-Allow-Origin", "*");
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        //标题
        String title = request.getParameter("title");
        //职位
        String position = request.getParameter("position");
        String[] positionArr = position.split(",");
        StringBuffer policy = new StringBuffer();
        for(String str:positionArr){
            policy.append("position:").append(str).append(" ");
        }
        policy.append("1").append("of").append(positionArr.length);
        //上传用户
        String user = request.getParameter("user");
        //用户传递的关键词
        String userKeywords = request.getParameter("userKeywords");
        //文件名
        String fileName= files.get(0).getOriginalFilename();
        //文件类型
        String fileType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());

        ResponseBean responseBean = new ResponseBean();

        String tmpFilePath = serverFilePath+"tmpFile";
        //创建临时目录
        File tmpFileDir = new File(tmpFilePath);
        if(tmpFileDir.mkdirs()){

        }
        Long timestamp = new Date().getTime();
        File tmpFile = new File(tmpFilePath+timestamp+fileName);
        try {
            files.get(0).transferTo(tmpFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


        String[] userKeywordsArr = userKeywords.replaceAll("，",",").split(",");
        //根据不同文件类型获取文件内容
        String content = "";
        InputStream fis = null;
        try {
            fis = new FileInputStream(tmpFile);
            if (fileType.equals("doc")) {
                HWPFDocument doc = new HWPFDocument(fis);
                content =  doc.getDocumentText();
            }else if(fileType.equals("docx")){
                XWPFDocument xdoc = new XWPFDocument(fis);
                XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
                content = extractor.getText();
            }else if(fileType.equals("txt")){
                content = FileUtil.readToString(tmpFile);
            }else if(fileType.equals("pdf")){
                PDDocument document =  PDDocument.load(tmpFile);
                // 读文本内容
                PDFTextStripper stripper=new PDFTextStripper();
                // 设置按顺序输出
                stripper.setSortByPosition(true);
                stripper.setStartPage(1);
                stripper.setEndPage(document.getNumberOfPages());
                content = stripper.getText(document);
                if(document!=null){
                    document.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileInfo file = new FileInfo();
        file.setPosition(positionArr);
        //如果用户填写了标题，就使用用户的标题
        if(title==null||title.trim().length()==0){
            title = fileName.substring(0,fileName.lastIndexOf("."));
        }
        file.setTitle(title);
        file.setUploadTime(new Date());
        file.setDownloadTimes(0);
        file.setFileType(fileType);
        file.setUser(user);
        try {
            //获取文件的md5码
            String md5Code = MD5Util.getMd5CodeByStr(content);
            //判断文件是否存在
            FileInfo fileInfo = cpabeRepository.findByMd5Code(md5Code);
            if (fileInfo!=null){
                //文件重复
                return "202";
            }
            file.setMd5Code(md5Code);

            //从文本中提取5个权重最高的关键词
            List<String> keywords = HanLP.extractKeyword(content,10);
            List<String> keywordsMd5 = new ArrayList<String>();
            for(String keyword:keywords){
                logger.info("文档提取的关键词："+keyword);
                keywordsMd5.add(MD5Util.getMd5CodeByStr(keyword));
            }
            //用户设置的关键词进行MD5编码
            for(String keyword:userKeywordsArr){
                if(!keywords.contains(keyword)){
                    keywordsMd5.add(MD5Util.getMd5CodeByStr(keyword));
                }
            }
            file.setKeywords(keywordsMd5);

            //获取摘要
            List<String> sentenceList = HanLP.extractSummary(content, 5);
            StringBuffer sb = new StringBuffer();
            for(String s:sentenceList){
                sb.append(s).append(",");
            }
            file.setSummary(sb.toString());
            //文件加密并保存
            if(encodeFile(md5Code,title+"."+fileType,policy.toString(),tmpFile)){
                cpabeRepository.save(file);
                logger.info("文档上传成功！");
            }else{
                logger.info("文档上传失败！");
                  return "201";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "201";
        }

        return "200";
    }


    /**
     * 生成源文件并加密
     * @param md5Code
     * @param fileName
     * @param policy
     * @return
     */
    public boolean encodeFile(String md5Code,String fileName,String policy,File sourceFile){
        FileWriter fileWriter=null;
        try {
            //创建文件夹
            File dir = new File(serverFilePath+md5Code);
            if(dir.mkdirs()){ }
            boolean result = DemoForCpabe.encode(policy,serverFilePath+md5Code,fileName,sourceFile.getPath());
            logger.info("文件加密结果："+result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }

    /**
     * 文件预览
     * @param request
     * @return
     */
    @RequestMapping("/showDoc")
    @ResponseBody
    public Object showDoc(HttpServletRequest request) {
        String md5Code = request.getParameter("md5Code");
        FileInfo fileInfo = this.cpabeRepository.findByMd5Code(md5Code);
        String attr = fileInfo.getPosition()[0];
        String content = "";
        /*
        解密并生成源文件
         */
        boolean result = false;
        try {
            result = DemoForCpabe.decode("position:"+attr,md5Code,fileInfo.getTitle()+"."+fileInfo.getFileType(),serverFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("文件预览功能，文件解密结果："+result);
        if(result) {
            File file= new File(serverFilePath+md5Code+"/dec-"+fileInfo.getTitle()+"."+fileInfo.getFileType());
            try {
                //文件内容获取，word转换成html文件，获取内容
                if (fileInfo.getFileType().equals("doc") || fileInfo.getFileType().equals("docx")) {
                    content = Word2Html2.toHtmlString(file, serverFilePath + md5Code+"/html");
                } else if (fileInfo.getFileType().equals("pdf")) {
                    // 读取pdf文件内容
                    PDFTextStripper stripper = null;
                    try {
                        PDDocument document = PDDocument.load(file);
                        stripper = new PDFTextStripper();
                        stripper.setSortByPosition(true);
                        stripper.setStartPage(1);
                        stripper.setEndPage(document.getNumberOfPages());
                        content = stripper.getText(document);
                        if (document != null) {
                            document.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    content = FileUtil.readToString(file);
                }

                String base64Str = Base64.encodeBase64String(content.getBytes());
                fileInfo.setContent(base64Str);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                //删除解密后的文件
                if (file.exists()) {
                    file.delete();
                }
            }
        }
        return fileInfo;
    }
    /**
     * 文件下载
     * @param request
     * @return
     */
    @RequestMapping("/download")
    public void download(HttpServletRequest request,HttpServletResponse response) {
        //文档id
        String md5Code = request.getParameter("md5Code");
        //用户名
        String userName = request.getParameter("userName");
        //查询文档信息
        FileInfo fieInfo = cpabeRepository.findByMd5Code(md5Code);
        //用来解密的属性
        String attr = "position:"+fieInfo.getPosition()[0];
        if(fieInfo!=null){
            try {
                String fileName = fieInfo.getTitle()+"."+fieInfo.getFileType();
                //解密文件
                boolean result = DemoForCpabe.decode(attr,md5Code,fileName,serverFilePath);
                if(result){
                    logger.info("解密成功，开始下载文件...");
                    File file= new File(serverFilePath+md5Code+"/dec-"+fileName);
                    StringBuffer content = new StringBuffer();
                    //读取解密后的文件并生成base64编码
                    if (file.exists()) { // 文件存在
                            /*读取文件*/
                            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                            byte[] buffer = new byte[inputStream.available()]; // int available() 返回值为流中尚未读取的字节的数量
                            inputStream.read(buffer);
                            inputStream.close();
                            response.reset();
                            response.addHeader("Content-Disposition",
                                    "attachment;filename=" + new String(fileName.getBytes("utf-8"), "iso8859-1"));
                            response.addHeader("Content-Length", "" + file.length());
                            /*输出文件*/
                            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
                            response.setContentType("application/octet-stream");
                            outputStream.write(buffer);
                            outputStream.flush();
                            outputStream.close();
                            file.delete();
                        }
                        //更新文章的下载次数
                        fieInfo.setDownloadTimes(fieInfo.getDownloadTimes()+1);
                        this.cpabeRepository.save(fieInfo);
                        //保存“我的下载记录”
                        if(userName!=null){
                            MyDownload download = new MyDownload();
                            download.setUserName(userName);
                            download.setAuthor(fieInfo.getUser());
                            download.setTitle(fieInfo.getTitle());
                            download.setFileType(fieInfo.getFileType());
                            download.setMd5Code(md5Code);
                            download.setDownloadTime(new Date());
                            this.myDownloadRepository.save(download);
                        }
                    }else {
                    logger.info("解密文件失败，取消下载...");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/testSave")
    @ResponseBody
    public String testSave(HttpServletRequest request, ModelMap model){
        String[] types={"doc","txt","pdf"};

        for(int i=1;i<100;i++){
           FileInfo file = new FileInfo();
           file.setTitle("论文四--基于属性的原型实现"+i);
           if(i<30){
               String[] position={"assistant","lecturer"};
                file.setPosition(position);
           }else if(i<=30&&i<50){
               String[] position={"assistant","professor"};
               file.setPosition(position);
            }else if(i<=50&&i<70){
               String[] position={"lecturer","professor"};
               file.setPosition(position);
           }else{
               String[] position={"assistant"};
               file.setPosition(position);
           }

            file.setFileType(types[(int)Math.floor(Math.random()*3)]);
            file.setDownloadTimes(i+100);
           file.setMd5Code("1234567890");
            file.setSummary("人工智能不是人的智能, 人工智能研究的一个主要目标是使机器能够胜任一些通常需要人类智能才能完成的复杂工作, 它是研究、开发用于模拟、延伸和扩展人的智能的理论、方法、技术及应用系统的一门新的技术科学, 它由不同的领域组成, 并生产出一种新的能以人类智能相似的方式做出反应的智能机器");
           file.setUploadTime(new Date());
           file.setUser("yangxin");
           StringBuffer str = new StringBuffer();
            List<String> keywords = new ArrayList<String>();
           if(i%2==0){
               keywords.add(MD5Util.getMd5CodeByStr("中国"));
           }
           if(i%3==0){
               keywords.add(MD5Util.getMd5CodeByStr("印度"));
           }
           if(i%5==0){
               keywords.add(MD5Util.getMd5CodeByStr("人民"));
           }
           if(i%7==0){
               keywords.add(MD5Util.getMd5CodeByStr("和平"));
            }
            keywords.add(MD5Util.getMd5CodeByStr("加密"));
           file.setKeywords(keywords);
           /*
           *save方法和insert方法都可以向数据库插入数据，区别是如果当前对象已经存在数据库中，则save()方法可以更新该对象，而insert()方法则忽略此次操作。
           * 如果当前对象在数据库中不存在则均可以向数据库插入对象
            */
            cpabeRepository.save(file);
        }
        return "ok";
    }

    @RequestMapping("/testSave2")
    @ResponseBody
    public String testSave2(HttpServletRequest request, ModelMap model){
        String[] types={"doc","txt","pdf"};

        for(int i=1;i<30;i++){
            MyDownload download = new MyDownload();
            download.setTitle("论文四--基于属性的原型实现"+i);
            download.setFileType(types[(int)Math.floor(Math.random()*3)]);
            download.setMd5Code("1234567890");
            download.setAuthor("yangxin1111");
            download.setUserName("yangxin");
            download.setDownloadTime(new Date());
           /*
           *save方法和insert方法都可以向数据库插入数据，区别是如果当前对象已经存在数据库中，则save()方法可以更新该对象，而insert()方法则忽略此次操作。
           * 如果当前对象在数据库中不存在则均可以向数据库插入对象
            */
            myDownloadRepository.save(download);
        }
        return "ok";
    }



    public static void main(String[] args){
//        List<Term> termList = HanLP.segment("商品和服务");
//        System.out.println(termList);
//
//        ForkJoinWorkerThread
      String str = "人工智能（Artificial Intelligence），英文缩写为AI。它是研究、开发用于模拟、延伸和扩展人的智能的理论、方法、技术及应用系统的一门新的技术科学。\n" +
              "人工智能是计算机科学的一个分支，它企图了解智能的实质，并生产出一种新的能以人类智能相似的方式做出反应的智能机器，该领域的研究包括机器人、语言识别、图像识别、自然语言处理和专家系统等。人工智能从诞生以来，理论和技术日益成熟，应用领域也不断扩大，可以设想，未来人工智能带来的科技产品，将会是人类智慧的“容器”。人工智能可以对人的意识、思维的信息过程的模拟。人工智能不是人的智能，但能像人那样思考、也可能超过人的智能。\n" +
              "人工智能是一门极富挑战性的科学，从事这项工作的人必须懂得计算机知识，心理学和哲学。人工智能是包括十分广泛的科学，它由不同的领域组成，如机器学习，计算机视觉等等，总的说来，人工智能研究的一个主要目标是使机器能够胜任一些通常需要人类智能才能完成的复杂工作。但不同的时代、不同的人对这种“复杂工作”的理解是不同的。[1]  2017年12月，人工智能入选“2017年度中国媒体十大流行语”。";

        List<String> sentenceList = HanLP.extractSummary(str, 5);

        System.out.println(sentenceList);


        List<Term> termList2 = NLPTokenizer.segment("中国科学院计算技术研究所的宗成庆教授正在教授自然语言处理课程");
        System.out.println(termList2);
//        ExecutorService executorService = Executors.newFixedThreadPool(5);
//        for(int i=0;i<10;i++){
//            executorService.execute(new TestRunnable());
//        }

    }

}
