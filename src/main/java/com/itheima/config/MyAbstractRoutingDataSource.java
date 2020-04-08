package com.itheima.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/*
 * @description: $param$
 * @date: $date$ $time$
 * @auther: pjy
 */
public class MyAbstractRoutingDataSource extends AbstractRoutingDataSource {
//    @Value("${mysql.datasource.num}")
//    private int num;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected Object determineCurrentLookupKey() {
        String typeKey = DbContextHolder.getDbType();
        if (typeKey == DbContextHolder.WRITE) {
            log.info("使用了写库");
            return typeKey;
        }
        else{
             log.info("使用了读库");
             return  DbContextHolder.READ;
        }
        //使用随机数决定使用哪个读库
//        int sum = NumberUtil.getRandom(1, num);
//        log.info("使用了读库{}", sum);
//        return DbContextHolder.READ + sum;
    }

}
