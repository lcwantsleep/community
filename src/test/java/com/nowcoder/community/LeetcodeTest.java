package com.nowcoder.community;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeetcodeTest {
    public int jump(int[] nums) {
        int cur=0;
        int next=0;
        int ans=0;
        for(int i=0;i<nums.length-1;i++){
            next=Math.max(nums[i]+i,next);
            if(i==cur){
                cur=next;
                ans++;
            }
        }
        return ans;
    }

}
