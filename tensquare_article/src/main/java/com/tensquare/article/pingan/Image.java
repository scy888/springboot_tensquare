package com.tensquare.article.pingan;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.pingan
 * @date: 2020-05-02 11:40:14
 * @describe: 图片信息dto
 */
public class Image implements Serializable {
    private static final long serialVersionUID = -6001422874532631133L;
    private String imageId;
    private String imageName;
    private String imageEnd;
    private String imageType;
    private Date createDate;
    private String imageSize;
    private List<String> lists;
    private List<Image> imageList;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageEnd() {
        return imageEnd;
    }

    public void setImageEnd(String imageEnd) {
        this.imageEnd = imageEnd;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public List<String> getLists() {
        return lists;
    }

    public void setLists(List<String> lists) {
        this.lists = lists;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }
}
