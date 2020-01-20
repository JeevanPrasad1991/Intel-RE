
package intelre.cpm.com.intelre.gsonGetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuditQuestion {

    @SerializedName("Question_Id")
    @Expose
    private Integer questionId;
    @SerializedName("Question")
    @Expose
    private String question;
    @SerializedName("Answer_Id")
    @Expose
    private Integer answerId;
    @SerializedName("Answer")
    @Expose
    private String answer;
    @SerializedName("Question_Category_Id")
    @Expose
    private Integer questionCategoryId;
    @SerializedName("Question_Category")
    @Expose
    private String questionCategory;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getQuestionCategoryId() {
        return questionCategoryId;
    }

    public void setQuestionCategoryId(Integer questionCategoryId) {
        this.questionCategoryId = questionCategoryId;
    }

    public String getQuestionCategory() {
        return questionCategory;
    }

    public void setQuestionCategory(String questionCategory) {
        this.questionCategory = questionCategory;
    }



    public String getCurrectanswerCd() {
        return currectanswerCd;
    }

    public String getCameraFLAG() {
        return cameraFLAG;
    }

    public void setCameraFLAG(String cameraFLAG) {
        this.cameraFLAG = cameraFLAG;
    }

    public String cameraFLAG;

    public void setCurrectanswerCd(String currectanswerCd) {
        this.currectanswerCd = currectanswerCd;
    }


    public String getCurrectanswer() {
        return currectanswer;
    }

    public void setCurrectanswer(String currectanswer) {
        this.currectanswer = currectanswer;
    }
    public String currectanswerCd;
    public String currectanswer;

    public String getAudit_cam() {
        return audit_cam;
    }
    @SerializedName("Image_Allow")
    @Expose
    private String  imageAllow;

    public String getImageAllowforanswer() {
        return imageAllowforanswer;
    }

    public void setImageAllowforanswer(String imageAllowforanswer) {
        this.imageAllowforanswer = imageAllowforanswer;
    }

    @SerializedName("ImageAllow")
    @Expose
    private String  imageAllowforanswer;


    public void setAudit_cam(String audit_cam) {
        this.audit_cam = audit_cam;
    }

    public String audit_cam="";

    public String  getImageAllow() {
        return imageAllow;
    }

    public void setImageAllow(String imageAllow) {
        this.imageAllow = imageAllow;
    }

}
