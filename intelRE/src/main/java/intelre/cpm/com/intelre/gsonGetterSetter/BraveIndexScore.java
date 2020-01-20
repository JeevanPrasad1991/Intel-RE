package intelre.cpm.com.intelre.gsonGetterSetter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class BraveIndexScore {
    @SerializedName("Store_Id")
    @Expose
    private Integer storeId;
    @SerializedName("Time_Period")
    @Expose
    private String timePeriod;
    @SerializedName("SM_Score")
    @Expose
    private Double sMScore;
    @SerializedName("TNA_Score")
    @Expose
    private Double tNAScore;
    @SerializedName("Visibility_Score")
    @Expose
    private Double visibilityScore;
    @SerializedName("Brave_Index")
    @Expose
    private Double braveIndex;

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public Double getSMScore() {
        return sMScore;
    }

    public void setSMScore(Double sMScore) {
        this.sMScore = sMScore;
    }

    public Double getTNAScore() {
        return tNAScore;
    }

    public void setTNAScore(Double tNAScore) {
        this.tNAScore = tNAScore;
    }

    public Double getVisibilityScore() {
        return visibilityScore;
    }

    public void setVisibilityScore(Double visibilityScore) {
        this.visibilityScore = visibilityScore;
    }

    public Double getBraveIndex() {
        return braveIndex;
    }

    public void setBraveIndex(Double braveIndex) {
        this.braveIndex = braveIndex;
    }
}
