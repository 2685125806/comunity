package com.twinkle.community.service;

import com.twinkle.community.dto.PagenationDTO;
import com.twinkle.community.dto.QuestionDTO;
import com.twinkle.community.exception.CustomizeErrorCode;
import com.twinkle.community.exception.CustomizeException;
import com.twinkle.community.mapper.QuestionMapper;
import com.twinkle.community.mapper.UserMapper;
import com.twinkle.community.model.Question;
import com.twinkle.community.model.QuestionExample;
import com.twinkle.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     *
     * @param page  当前页码
     * @param size  每页显示条数
     * @return pagenationDTO 一个pagenationDTO包含question+user+分页这些需要的内容
     */
    public PagenationDTO getQuestionList(Integer page, Integer size) {
        /*
        * page  size  offset
        * 1       5     0
        * 2       5     5
        * 3       5     10
        * */
        //size (page-1) SQl:select * from question limit offset,size;
        /*offset:每次查询的起始页数吧 看上边*/
        Integer offset = size*(page-1);
//      List<Question> questionList = questionMapper.getQuestionList(size,offset);
       // List<Question> questionList   = questionMapper.selectByExampleWithRowbounds(new QuestionExample(),new RowBounds(offset,size));
        List<Question> questionList   = questionMapper.selectByExampleWithBLOBsWithRowbounds(new QuestionExample(),new RowBounds(offset,size));
/*
         for (int i=0;i<questionList.size();i++){
             System.out.println("--"+questionList.get(i).getTitle()+"--"+questionList.get(i).getDescription());
         }*/

       /*QuestionDTO包含问题相关信息及user对象,每个question的具体内容还要对应一个发布者(user)*/
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PagenationDTO pagenationDTO = new PagenationDTO();

        /*分别将问题查询出来并且根据creator查询出user再存入questionDTO,
        循环将所有的questionDTO存入List中*/
        for (Question question: questionList){
//            User user = userMapper.findById(question.getCreator());
            /*通过id查询user,id来自question的creator*/
            User user =  userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            check(questionDTO);
            questionDTOList.add(questionDTO);
        }
        /*把所以查询的问题及user存入到分页DTO中*/

        pagenationDTO.setQuestions(questionDTOList);


        QuestionExample questionExample = new QuestionExample();
        /*获取总记录数*/
        Integer count =(int)questionMapper.countByExample(questionExample);
        /*将参数传入到pagenationDTO做分页逻辑*/
        pagenationDTO.setPagenation(page,size,count);
        if (page>pagenationDTO.getTotalPage()){
            throw new CustomizeException(CustomizeErrorCode.PAGE_NOT_EXIST);
        }
        return pagenationDTO;
    }

    private void check(QuestionDTO questionDTO) {
        if (questionDTO.getDescription().length()>=80){
            questionDTO.setDescription(questionDTO.getDescription().substring(0,85)+"...");
        }
    }

    //根据id查询问题列表
    public PagenationDTO questionList(Integer Id,Integer page,Integer size) {
        //size (page-1) limit offset, size offset:每次分頁起始個數,
        // 第1頁 1-5,第二頁,6-10,offset:1 6
        Integer offset = size*(page-1);

        /*获取id*/
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(Id);
        System.out.println("QSID1---");
        /*根据id查询问题并做分页处理 下边大多同上*/
        List<Question> questionList = questionMapper.selectByExampleWithBLOBsWithRowbounds(example,new RowBounds(offset,size));
        for (Question ques: questionList) {
            System.out.println("QSID2---"+ques);
        }

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PagenationDTO pagenationDTO = new PagenationDTO();
        for (Question question: questionList){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            check(questionDTO);
            questionDTOList.add(questionDTO);
        }
        pagenationDTO.setQuestions(questionDTOList);
        Integer count = (int)questionMapper.countByExample(example);
        pagenationDTO.setPagenation(page,size,count);
        System.out.println("QSID3---");
        return pagenationDTO;
    }

    public QuestionDTO getQuestionById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question ==null){
           throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;

    }

    public void createOrUpdate(Question question) {
        if (question.getId()==null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtUpdate(question.getGmtCreate());
            questionMapper.insert(question);

        }else {
            question.setGmtCreate(question.getGmtCreate());
            question.setGmtUpdate(System.currentTimeMillis());
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());
            questionMapper.updateByPrimaryKey(question);
        }
    }
}
