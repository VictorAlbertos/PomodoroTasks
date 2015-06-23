package models;

public class Card extends BaseModel {
    protected String idList, desc;

    public String getIdList() {
        return idList;
    }
    public void setIdList(String idList) {
        this.idList = idList;
    }

    public String getDesc() {
        return desc;
    }
}
