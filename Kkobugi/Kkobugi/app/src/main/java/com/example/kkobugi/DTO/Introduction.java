package com.example.kkobugi.DTO;

public class Introduction {

    private int introductionIdx;
    private String introductionName;
    private String introductionContent;
    private String introImage;

    public int getIntroductionIdx(){
        return introductionIdx;
    }

    public void setIntroductionIdx(int introductionIdx){
        this.introductionIdx = introductionIdx;
    }

    public String getIntroductionName(){
        return introductionName;
    }

    public void setIntroductionName(String introductionName){
        this.introductionName = introductionName;
    }

    public String getIntroductionContent(){
        return introductionContent;
    }

    public void setIntroductionContent(String introductionContent){
        this.introductionContent = introductionContent;
    }

    public String getIntroImage(){
        return introImage;
    }

    public void setIntroImage(String introImage){
        this.introImage = introImage;
    }

}
