package intelre.cpm.com.intelre.gsonGetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PosmTypeQuestion {
    @SerializedName("Question_Id")
    @Expose
    private Integer questionId;
    @SerializedName("Question")
    @Expose
    private String question;
    @SerializedName("Posm_Type_Id")
    @Expose
    private Integer posmTypeId;
    @SerializedName("Answer_Id")
    @Expose
    private Integer answerId;
    @SerializedName("Answer")
    @Expose
    private String answer;
    @SerializedName("Image_Allow")
    @Expose
    private Boolean imageAllow;
    @SerializedName("Question_Disable")
    @Expose
    private Integer questionDisable;

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

    public Integer getPosmTypeId() {
        return posmTypeId;
    }

    public void setPosmTypeId(Integer posmTypeId) {
        this.posmTypeId = posmTypeId;
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

    public Boolean getImageAllow() {
        return imageAllow;
    }

    public void setImageAllow(Boolean imageAllow) {
        this.imageAllow = imageAllow;
    }

    public Integer getQuestionDisable() {
        return questionDisable;
    }

    public void setQuestionDisable(Integer questionDisable) {
        this.questionDisable = questionDisable;
    }

    public String getPosmType() {
        return posmType;
    }

    public void setPosmType(String posmType) {
        this.posmType = posmType;
    }

    private String posmType;

    public String getSemipmerch_img() {
        return semipmerch_img;
    }

    public void setSemipmerch_img(String semipmerch_img) {
        this.semipmerch_img = semipmerch_img;
    }

    String semipmerch_img = "";

    public String getSemip_right_ans_Id() {
        return semip_right_ans_Id;
    }

    public void setSemip_right_ans_Id(String semip_right_ans_Id) {
        this.semip_right_ans_Id = semip_right_ans_Id;
    }

    String semip_right_ans_Id = "0";

    public String getSemip_right_ans() {
        return semip_right_ans;
    }

    public void setSemip_right_ans(String semip_right_ans) {
        this.semip_right_ans = semip_right_ans;
    }

    String semip_right_ans="";

    public String getSemip_currectimageAllow() {
        return semip_currectimageAllow;
    }

    public void setSemip_currectimageAllow(String semip_currectimageAllow) {
        this.semip_currectimageAllow = semip_currectimageAllow;
    }

    String semip_currectimageAllow = "0";

    public String getHeader_availebility() {
        return header_availebility;
    }

    public void setHeader_availebility(String header_availebility) {
        this.header_availebility = header_availebility;
    }

    String header_availebility = "0";

    public String getHeader_image() {
        return header_image;
    }

    public void setHeader_image(String header_image) {
        this.header_image = header_image;
    }

    String header_image = "";
    String deployment_date = "";

    public String getDeployment_date() {
        return deployment_date;
    }

    public void setDeployment_date(String deployment_date) {
        this.deployment_date = deployment_date;
    }

    public String getDeployment_flag() {
        return deployment_flag;
    }

    public void setDeployment_flag(String deployment_flag) {
        this.deployment_flag = deployment_flag;
    }

    String deployment_flag = "0";

    public String getInstallation_date() {
        return installation_date;
    }

    public void setInstallation_date(String installation_date) {
        this.installation_date = installation_date;
    }

    String installation_date="";

    public String getSoft_merch_name() {
        return soft_merch_name;
    }

    public void setSoft_merch_name(String soft_merch_name) {
        this.soft_merch_name = soft_merch_name;
    }

    String soft_merch_name="";

    public String getPromoType_qty() {
        return promoType_qty;
    }

    public void setPromoType_qty(String promoType_qty) {
        this.promoType_qty = promoType_qty;
    }

    String promoType_qty="";

    public String getKey_Id() {
        return key_Id;
    }

    public void setKey_Id(String key_Id) {
        this.key_Id = key_Id;
    }

    String key_Id;

}
