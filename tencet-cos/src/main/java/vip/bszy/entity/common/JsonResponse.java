package vip.bszy.entity.common;

import lombok.Data;

/**
 * @author ：李延富
 * @date ：Created in 2019/12/17 23:24
 * description ：
 */
@Data
public class JsonResponse {
    private Integer code;
    private String message;
    private Object data;

    public JsonResponse() {
        this.code = 200;
        this.message = "ok";
    }

    public JsonResponse(Object data) {
        this.code = 200;
        this.message = "ok";
        this.data = data;
    }

    public JsonResponse(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
