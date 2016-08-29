package zyf.inspur.com.coolweather.model;

/**
 * Created by zyf on 2016/8/23.
 */
public class Province {
    private int id;
    private String provinceName;
    private String provinceCode;


    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

}
