package cn.enncloud.iot.iotgatewaymodbus.http.response;

import java.util.List;

/**
 * @description:    分页响应信息体
 * @author:         zdl
 * @createDate:     2018/7/31 17:24
 * @updateUser:     zdl
 * @updateDate:     2018/7/31 17:24
 * @updateRemark:   修改内容
 * @version:        1.0
 */
public class DataRespWithPage<T extends List> extends ResponseBody {

    private T data;

    private long totalCount;

    private int page;

    private int limit;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
