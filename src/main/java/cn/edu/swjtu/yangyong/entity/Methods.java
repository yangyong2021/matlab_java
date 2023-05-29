package cn.edu.swjtu.yangyong.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Yangyong
 * 2023-05-29 15:59
 */
@Data
public class Methods implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String mainName;
    private String dataName;
    private Integer status;
    private String image;
    private String methods;
}
