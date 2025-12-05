package cn.edu.usst.mud;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import java.io.Serializable;

public abstract class DigitalLife implements Serializable {
    private static final long serialVersionUID = 1L;//序列化版本号，如果不写则添加新字段时自动生成的地址会变化，导致无法访问旧地址。
    protected String name;
    protected int hp;//发量
    protected int maxHp;//最大发量
    protected boolean hasBatthleExp;

    //构造方法:创建对象并且初始化对象属性
    public DigitalLife(String name, int hp) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
    }

    //成员方法1：头发还在吗？
    public boolean isAlive() {
        return this.hp > 0;
    }

    //成员方法2:掉头发
    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) this.hp = 0;
        if (this.hp > this.maxHp) this.hp = this.maxHp; // 防止回血溢出

        if (damage > 0) {
            System.out.println("   -> " + this.name + " 掉了 " + damage + " 根头发！(剩余: " + this.hp + "/" + this.maxHp + ")");
        } else if (damage < 0) {
            System.out.println("   -> " + this.name + " 植发成功，恢复了 " + (-damage) + " 根头发！(剩余: " + this.hp + "/" + this.maxHp + ")");
        }
    }

//    //getter setter进行值的改变
//    public String getName() {
//        this.name = name;
//        return null;
//    }
//
//    public int getHp() {
//        this.hp = hp;
//        return 0;
//    }
//
////    public void getAttack(int attack) {
////        this.attack = attack;
////    }
//
//    public void getMaxHp(int hp) {
//        this.maxHp = hp;
//    }
//
//    public boolean isHasBattleExp() {
//        return false;
//    }
    public String getName() { return this.name; }
    public int getHp() { return this.hp; }
    public int getMaxHp() { return this.maxHp; }

}

//玩家类
class Programmer extends DigitalLife{
    private List<Skill>skills;
    private int level;
    private int trainCount;
    private boolean hasBattleExp;

    public Programmer(String name,int hp){
        super(name,hp);
        this.level=1;
        this.trainCount=0;
        this.skills=new ArrayList<>();
        this.hasBatthleExp=false;
        this.skills.add(new Skill("普通攻击",10));
    }

    //躲避（随机）
    public boolean dodge(){
        Random r=new Random();
        return r.nextInt(100)<20;//20%概率躲避成功
    }
    //练习技能
    public void train(){
        System.out.println("正在埋头苦读文档...");
        this.trainCount++;
        //提升所有技能熟练度
        for(Skill s: skills){
            s.increaseMastery();
        }
        checkLevelUp();
    }
    //训练次数到等级值及新工具的转换逻辑
    private void checkLevelUp(){
        // LV1 -> LV2: 训练3次
        if(level==1&&trainCount>=3){
            levelUp(2);
        }
        //LV2->LV3:训练五次
        else if(level==2&&trainCount>=3){
            levelUp(3);
        }
        else if(level==3&&trainCount>=7&&hasBattleExp){
            levelUp(5);
        }
        else {
            System.out.println(">>当前训练进度："+trainCount+"次(等级: " + level + ")");
        }
    }
    //具体实践
    private void levelUp(int newLv){
        this.level=newLv;
        this.trainCount=0;
        this.hasBattleExp=false;
        System.out.println("\n========== 升级了！ ==========");
        System.out.println("恭喜 " + name + " 升到了 LV" + level);
        //等级值->解锁新技能
        if(level>=3){
            boolean hasDebugger=false;
            for(Skill s:skills){
                if(s.getName().equals("Debugger")){
                    hasDebugger=true;
                }
            }
            if(!hasDebugger){
                System.out.println(">> 【解锁新技能】：调试器！");
                skills.add(new Skill("调试器", 9999));
            }
        }
        System.out.println("==============================\n");

    }
//    //标记完成一次实战
//    public void completeBattle() {
//        this.hasBattleExp=true;
//        checkLevelUp();//检查战后是否满足升级条件
//    }
//    public int getLevel(){
//        return level;
//    }

    public List<Skill> getSkills() {
        return skills;
    }

    // 【新增】用于读取存档的 Setter 方法
    public void setLevel(int level) { this.level = level; }
    public void setTrainCount(int count) { this.trainCount = count; }
    public void setHasBattleExp(boolean has) { this.hasBattleExp = has; }

    // 【新增】重置技能列表 (读取存档时先清空默认技能)
    public void clearSkills() { this.skills.clear(); }
    public void addSkill(Skill s) { this.skills.add(s); }

    // 用于存档的 Getter
    public int getLevel() { return level; }
    public int getTrainCount() { return trainCount; }
    public boolean isHasBattleExp() { return hasBattleExp; }

    public void completeBattle() {

    }
//    public boolean isHasBattleExp() {
//        return this.hasBattleExp; // 返回属性值，而非打印
//    }
    public String getName() { return name;  }
    public int getHp() { return hp; }

}

//Bug类
class Bug extends DigitalLife {
    public Bug(String name,int hp){
        super(name,hp);
    }
    //攻击
    public int attack(){
        return 5+ new Random().nextInt(10);
    }
    //躲避
    public boolean dodge(){
        return new Random().nextInt(100)<10;
    }
}