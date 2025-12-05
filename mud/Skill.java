package cn.edu.usst.mud;

import java.io.Serializable;
import java.util.Random;

public class Skill implements Serializable {
    private String name;
    private int baseDamage;//基础debug
    private int level;//debug能力级别
    private int mastery;//debug熟练度
    //初始化
    public Skill(String name,int baseDamage){
        this.name=name;
        this.baseDamage=baseDamage;
        this.level=level;
        this.mastery=mastery;
    }
    //熟练度提升
    public void increaseMastery(){
        this.mastery+=10;
        System.out.println("   >> 技能 [" + name + "] 熟练度 +10 (当前: " + mastery + "/100)");
        if(this.mastery>=100){
            this.level++;
            this.mastery=0;
            System.out.println("   ★★ 恭喜！技能 [" + name + "] 升级了！当前等级：LV" + level + " ★★");        }
    }
    //计算伤害（基础伤害*等级）
    public int calculateDamage(){
        if(this.name.equals("调试器")){
            return 9999;
        }
        Random r = new Random();
        int fluctuation = r.nextInt(5) - 2; // -2 到 +2
        return (baseDamage * level) + fluctuation;
    }
    //命中率计算(返回是否命中)
    public boolean isHit(){
        /*if(this.level==1){
            Random r1=new Random();
            return r1.nextInt(100)<=50;
        }
        else if(this.level==2){
            Random r2=new Random();
            return r2.nextInt(100)<=60;
        }
        else if (this.level==3){
            Random r3=new Random();
            return r3.nextInt(100)<=70;
        }*/
//        int chance=50+(level-1)*10;
//        if (chance > 90) {
//            chance=90;
//        }
//        Random r=new Random();//创建Random类对象
//        return r.nextInt(100)<chance;//调用该对象nextInt方法
        int chance = 60 + (level * 5);
        if (chance > 95) chance = 95;
        return new Random().nextInt(100) < chance;
    }
    //
    public String getName(){return name;}
    public int getBaseDamage() { return baseDamage; }
    public int getLevel() { return level; }
    public int getMastery() { return mastery; }





}
