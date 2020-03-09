package com.twinkle.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PagenationDTO {
    private List<QuestionDTO> questions;//问题列表+发布者
    private Boolean showFirst;      //首页
    private Boolean showEndPage;    //尾页
    private Boolean showPrevious;  //前一页
    private Boolean showNext;       //下一页
    private Integer page;   //当前页
    private Integer totalPage;//总页数 count/size
    private List<Integer> pages = new ArrayList<>();

    /**
     * @param page 当前页
     * @param size  每页多少条
     * @param count  总记录数
     */
    public void setPagenation(Integer page, Integer size, Integer count) {

        this.page=page;
        /*模运算的结果为0,能够整除,不为零就要+1取整,得到总页数*/
        if (count%size == 0){
            totalPage = count /size;
        }else{
            totalPage = (count / size)+1;
        }
        //显示首页
        if (page == 1){
            showFirst = false;
            showPrevious = false;
        }else{
            showPrevious = true;
            if (page>3){
                showFirst = true;
            }
        }
        //显示尾页
        if (page == totalPage){
            showEndPage = false;
            showNext = false;
        }else {
            showNext = true;
            if (page<(totalPage - 3)){
                showEndPage = true;
            }
        }
//f分页有问题,需要及时处理
        if(page>3 ){
            int first = page-2;
            Integer temp =null;
            for (int i=0;i<5;i++){
               temp =(first+i);
               if (temp>totalPage){
                   break;
               }
                pages.add(i, (first+i));
            }

//            System.out.println("if----");
        }else{
//            System.out.println("else------");
            for (int i=0;i<5;i++){
                pages.add(i, (i+1));
                System.out.println(pages);
            }
        }
    }

}
