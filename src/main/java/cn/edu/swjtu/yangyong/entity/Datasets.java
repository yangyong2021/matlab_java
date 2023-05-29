package cn.edu.swjtu.yangyong.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Yangyong
 * 2023-05-29 12:06
 */
@Data
public class Datasets implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String label;
    private String value;
}
