package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SentitiveFilter {
    private static final Logger logger= LoggerFactory.getLogger(SentitiveFilter.class);
    //替换常量
    private static final String REPLACEMENT="***";
    //初始化树
    private TrieNode rootNode=new TrieNode();
    @PostConstruct
    public void init(){

        try (
                InputStream is =this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader=new BufferedReader(new InputStreamReader(is));
                ){
            String keyword;
            while ((keyword=reader.readLine())!=null){
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            logger.error("加载敏感词文件："+e.getMessage());
        }
    }
    //将敏感词添加到前缀树中
    private void addKeyword(String keyword){
        TrieNode tempNode=rootNode;
        for(int i=0;i<keyword.length();i++){
            char c=keyword.charAt(i);
            TrieNode subNode=tempNode.getSubNode(c);
            if(subNode==null){
                //初始化子节点
                subNode=new TrieNode();
                tempNode.addSubNode(c,subNode);
            }
            //指向子节点，进去下一轮循环
            tempNode=subNode;
            if(i==keyword.length()-1){
                tempNode.setKeywordEnd(true);
            }
        }
    }
    //
    public String fiter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }
        //指针1
        TrieNode tempNode=rootNode;
        //指针2
        int begin=0;
        //指针3
        int position=0;
        StringBuilder sb=new StringBuilder();
        while (begin<text.length()){
            if(position<text.length()){
                Character c=text.charAt(position);
                //跳过符号
                if(isSymbol(c)){
                    if(tempNode==rootNode){
                        begin++;
                        sb.append(c);
                    }
                    position++;
                    continue;
                }
                //检查下级
                tempNode=tempNode.getSubNode(c);
                if(tempNode==null){
                    //以begin开头的字符串不是敏感词
                    sb.append(text.charAt(begin));
                    //进入下一个位置
                    position=++begin;
                    //重新指向根节点
                    tempNode=rootNode;
                }
                //发现敏感词
                else if(tempNode.isKeywordEnd()){
                    sb.append(REPLACEMENT);
                    begin=++position;
                }else {
                    position++;//检查下一个字符
                }
            }else {//postition越界还没匹配到关键词
                sb.append(text.charAt(begin));
                position=++begin;
                tempNode=rootNode;
            }
        }
        return sb.toString();
    }
    //判断是否为符号
    private boolean isSymbol(Character c){
        return !CharUtils.isAsciiAlphanumeric(c)&&(c<0x2E80||c>0x9FFF);
    }
    //前缀树
    private class TrieNode{
        //关键词结束标志
        private boolean isKeywordEnd=false;
        //子节点
        private Map<Character,TrieNode> subNodes=new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }
        //添加子节点
        public void addSubNode(Character c,TrieNode node){
            subNodes.put(c,node);
        }
        //获取子节点
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }
}

