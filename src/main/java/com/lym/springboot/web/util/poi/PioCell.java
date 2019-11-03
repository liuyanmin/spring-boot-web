package com.lym.springboot.web.util.poi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by liuyanmin on 2019/10/12.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PioCell<T> {

    private int row;
    private int col;
    private T data;

}
