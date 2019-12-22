package com.twinkle.community.service;

import com.twinkle.community.dto.QuestionDTO;
import com.twinkle.community.mapper.QuestionMapper;
import com.twinkle.community.mapper.UserMapper;
import com.twinkle.community.model.Question;
import com.twinkle.community.model.User;
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

    public List<QuestionDTO> getQuestionList() {

        List<Question> questionList = questionMapper.getQuestionList();
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question: questionList){
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }
}
