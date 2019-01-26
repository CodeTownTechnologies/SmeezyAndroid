package com.app.smeezy.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaqItem {

    @SerializedName("f_id")
    @Expose
    private String fId;
    @SerializedName("faq_question")
    @Expose
    private String faqQuestion;
    @SerializedName("faq_answer")
    @Expose
    private String faqAnswer;

    public String getFId() {
        return fId;
    }

    public void setFId(String fId) {
        this.fId = fId;
    }

    public FaqItem withFId(String fId) {
        this.fId = fId;
        return this;
    }

    public String getFaqQuestion() {
        return faqQuestion;
    }

    public void setFaqQuestion(String faqQuestion) {
        this.faqQuestion = faqQuestion;
    }

    public FaqItem withFaqQuestion(String faqQuestion) {
        this.faqQuestion = faqQuestion;
        return this;
    }

    public String getFaqAnswer() {
        return faqAnswer;
    }

    public void setFaqAnswer(String faqAnswer) {
        this.faqAnswer = faqAnswer;
    }

    public FaqItem withFaqAnswer(String faqAnswer) {
        this.faqAnswer = faqAnswer;
        return this;
    }

}