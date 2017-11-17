package com.bassiouny.naqalati_driver.model;

/**
 * Created by bassiouny on 13/11/17.
 */

public interface RequestListener {
    void showMore(RequestInfo requestInfo);
    void accept(String userId);
    void refuse(String userId);
}
