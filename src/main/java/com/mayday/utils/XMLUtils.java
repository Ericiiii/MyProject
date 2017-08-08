package com.mayday.utils;




import com.mayday.entity.LotteryEntity;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * xml解析工具类
 *
 */
public class XMLUtils {

    //解析重庆时时彩xml
    public static List<LotteryEntity> getLotteryList(String xml) {
        Document doc = null;
        List<LotteryEntity> list=new ArrayList <LotteryEntity>();

        try {
            doc = DocumentHelper.parseText(xml); // 将字符串转为XML
            Element rootElt = doc.getRootElement(); // 获取根节点
            System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称
            Iterator iter = rootElt.elementIterator("row"); // 获取根节点下的子节点head
            // 遍历head节点


            int i=0;

            while (iter.hasNext()) {
                Element recordEle = (Element) iter.next();
                String pid = recordEle.attributeValue("pid"); // 拿到head节点下的子节点title值
                String acode=recordEle.attributeValue("acode");
                String atime=recordEle.attributeValue("atime");

                list.add(new LotteryEntity(pid,acode,atime));

            }

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }

}
