package com.ninelook.wecard.server.service.module.entity;

import com.ninelook.wecard.server.NException;
import com.ninelook.wecard.server.scene.player.EntityCampEnum;
import com.ninelook.wecard.server.service.handler.fight.inner.message.SDeadInnerMessage;
import com.ninelook.wecard.server.service.module.ai.SUnitAI;
import com.ninelook.wecard.server.service.module.entity.fight.SAttackEffectTypeEnum;
import com.ninelook.wecard.server.service.module.entity.fight.SDefenceFeatureEntity;

import java.util.ArrayList;
import java.util.Random;

/**
 * 可移动可攻击类的实体
 * User: Simon
 * Date: 2/12/14 16:10
 */
public abstract class SUnit extends SEntity {
    /**
     * ai控制对象
     */
    protected SUnitAI aiEngine;

    /**
     * 移动控制对象
     */
    protected SMovement movement;

    /**
     * 武器攻击对象
     */
    protected SWeaponAttack weaponAttack;

    //serverId
    protected int serverId;

    //等级
    protected int level = 1;

    //实体所处阵型
    protected EntityCampEnum camp;

    //技能ID
    protected int skillId;

    //血量
    protected int blood;

    //初始血量
    protected int srcBlood;

    //最大血量
    protected int maxBlood;

    //移动速度
    protected float speed;

    //初始移动速度
    protected float srcSpeed;

    //攻击力
    protected int attackNum;

    //初始攻击力
    protected int srcAttackNum;

    //魔法攻击力
    protected int magicAttackNum;

    //初始魔法攻击力
    protected int srcMagicAttackNum;

    //防御力
    protected int defense;

    //初始防御力
    protected int srcDefense;

    //魔法防御力
    protected int magicDefense;

    //初始魔法防御力
    protected int srcMagicDefense;

    //闪避
    protected int dodge;

    //初始闪避
    protected int srcDodge;

    //暴击
    protected int crit;

    //初始暴击
    protected int srcCrit;

    //攻击范围
    protected int range;

    //初始攻击范围
    protected int srcRange;

    //攻击距离
    protected int distance;

    //初始攻击距离
    protected int srcDistance;

    //敌人列表
    protected ArrayList<Integer> enemyList = new ArrayList<Integer>();

    /**
     * 出生序号
     */
    private int bornOrder;

    /**
     * 实体状态
     */
    private int status;

    //普通攻击技能
    protected int atkSkillId;

    // AI职业类型
    protected int jobAi;

    public SUnit(SEntityTypeEnum type) {
        super(type);
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getBlood() {
        return blood;
    }

    public void setBlood(int blood) {
        this.blood = blood;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getAttackNum() {
        return attackNum;
    }

    public void setAttackNum(int attackNum) {
        this.attackNum = attackNum;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public SMovement getMovement() {
        return movement;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getDodge() {
        return dodge;
    }

    public void setDodge(int dodge) {
        this.dodge = dodge;
    }

    public int getCrit() {
        return crit;
    }

    public void setCrit(int crit) {
        this.crit = crit;
    }

    public int getSrcBlood() {
        return srcBlood;
    }

    public void setSrcBlood(int srcBlood) {
        this.srcBlood = srcBlood;
    }

    public float getSrcSpeed() {
        return srcSpeed;
    }

    public void setSrcSpeed(float srcSpeed) {
        this.srcSpeed = srcSpeed;
    }

    public int getSrcAttackNum() {
        return srcAttackNum;
    }

    public void setSrcAttackNum(int srcAttackNum) {
        this.srcAttackNum = srcAttackNum;
    }

    public int getMagicAttackNum() {
        return magicAttackNum;
    }

    public void setMagicAttackNum(int magicAttackNum) {
        this.magicAttackNum = magicAttackNum;
    }

    public int getSrcMagicAttackNum() {
        return srcMagicAttackNum;
    }

    public void setSrcMagicAttackNum(int srcMagicAttackNum) {
        this.srcMagicAttackNum = srcMagicAttackNum;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getSrcDefense() {
        return srcDefense;
    }

    public void setSrcDefense(int srcDefense) {
        this.srcDefense = srcDefense;
    }

    public int getMagicDefense() {
        return magicDefense;
    }

    public void setMagicDefense(int magicDefense) {
        this.magicDefense = magicDefense;
    }

    public int getSrcMagicDefense() {
        return srcMagicDefense;
    }

    public void setSrcMagicDefense(int srcMagicDefense) {
        this.srcMagicDefense = srcMagicDefense;
    }

    public int getSrcDodge() {
        return srcDodge;
    }

    public void setSrcDodge(int srcDodge) {
        this.srcDodge = srcDodge;
    }

    public int getSrcCrit() {
        return srcCrit;
    }

    public void setSrcCrit(int srcCrit) {
        this.srcCrit = srcCrit;
    }

    public int getSrcRange() {
        return srcRange;
    }

    public void setSrcRange(int srcRange) {
        this.srcRange = srcRange;
    }

    public int getSrcDistance() {
        return srcDistance;
    }

    public void setSrcDistance(int srcDistance) {
        this.srcDistance = srcDistance;
    }

    public EntityCampEnum getCamp() {
        return camp;
    }

    public void setCamp(EntityCampEnum camp) {
        this.camp = camp;
    }

    public ArrayList<Integer> getEnemyList() {
        return enemyList;
    }

    public SWeaponAttack getWeaponAttack() {
        return weaponAttack;
    }

    public int getBornOrder() {
        return bornOrder;
    }

    public void setBornOrder(int bornOrder) {
        this.bornOrder = bornOrder;
    }

    public int getMaxBlood() {
        return maxBlood;
    }

    public void setMaxBlood(int maxBlood) {
        this.maxBlood = maxBlood;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAtkSkillId() {
        return atkSkillId;
    }

    public void setAtkSkillId(int atkSkillId) {
        this.atkSkillId = atkSkillId;
    }

    public int getJobAi() {
        return jobAi;
    }

    public void setJobAi(int jobAi) {
        this.jobAi = jobAi;
    }

    /**
     * 初始化
     */
    public void init() {
        //初始化攻击和移动
        if (type == SEntityTypeEnum.TYPE_CREATURE || type == SEntityTypeEnum.TYPE_HERO) {

        }

    }

    /**
     * 加血
     * @param       num
     * @return              实际加血数量
     */
    public int addBlood(int num) {
        int lastBlood = 0;
        lastBlood += num;
        if (lastBlood > maxBlood) lastBlood = maxBlood;

        int realAddNum = this.blood - lastBlood;

        this.blood = lastBlood;

        return realAddNum;
    }

    /**
     * 添加一个敌人
     * @param eid
     * @return
     */
    public boolean addEnemy(Integer eid) {
        for (Integer enemyEid : enemyList) {
            if (enemyEid == eid ) {
                return false;
            }
        }

        enemyList.add(eid);

        return true;
    }

    /**
     * 清除当前实体仇敌列表
     * @return
     */
    public boolean clearEnemy() {
        enemyList.clear();

        return true;
    }


    /**
     * 当前实体接收一次伤害
     * @param attackNum
     */
    public SDefenceFeatureEntity receiveAttack(int attackNum) {
        if (this.status != SEntityConstant.SENTITY_STATUS_NORMAL) {
            return null;
        }

        int defenceNum;
        if (this.magicDefense > 0) {
            defenceNum = this.magicDefense;
        } else {
            defenceNum = this.defense;
        }

        int realAttackNum = (attackNum * attackNum) / (attackNum + defenceNum);
        if (realAttackNum < 1)
            realAttackNum = 1;

        int beforeAttackHp = this.blood;

        //实际攻击力
        int realLossNum = 0;

        SDefenceFeatureEntity defenceFeatureEntity = new SDefenceFeatureEntity();

        Random rad = new Random();

        //暴击
        if (rad.nextInt(100) < this.crit) {
            defenceFeatureEntity.addEffect(SAttackEffectTypeEnum.TYPE_CRIT);
            realAttackNum *= 1.5;
        }

        //闪避
        if (rad.nextInt(100) < this.dodge) {
            defenceFeatureEntity.addEffect(SAttackEffectTypeEnum.TYPE_DODGE);
            realAttackNum = 0;
        }

        if (realAttackNum > 0) {
            realLossNum = deincrementBlood(realAttackNum);
        }

        //防御方防御效果
        defenceFeatureEntity.setLossNum(realLossNum);
        defenceFeatureEntity.setCurrentBloodNum(this.blood);
        defenceFeatureEntity.setEid(this.getEid());

        //todo:如果产生闪避则添加闪避效果

        return defenceFeatureEntity;
    }

    /**
     * 获取当前攻击实体各连招攻击力
     * @param serial
     * @return
     */
    public int getSerialAttackNum(int serial) {
        int baseAttack;
        if (this.magicAttackNum > 0) {
            baseAttack = this.magicAttackNum;
        } else {
            baseAttack = this.attackNum;
        }

        //如果实体是野怪, 则采用攻击序列为3
        if (this.type == SEntityTypeEnum.TYPE_CREATURE) {
            serial = 3;
        }

        if (serial == 1) {
            baseAttack *= 0.2;
        } else if (serial == 2) {
            baseAttack *= 0.3;
        } else if (serial == 3) {
            baseAttack *= 0.5;
        } else if (serial == 4) {
            baseAttack *= 0.8;
        } else if (serial == 5) {
            baseAttack *= 1.5;
        }

        if (baseAttack <= 1) {
            baseAttack = 1;
        }

        return baseAttack;
    }

    /**
     * 获取当前实体防御力
     * @return
     */
    public int getDefenceNum() {
        return this.defense;
    }

    /**
     * 当前实体承受一次攻击
     * @return
     */
    public boolean attack() {
        return weaponAttack.attack(1);
    }

    /**
     * 当前实体承受一次攻击
     * @return
     */
    public boolean attack(int attackSerial) {
        if (weaponAttack == null) {
            throw new NException(NException.SERVICE_FIGHT_NOT_WEAPON);
        }

        return weaponAttack.attack(attackSerial);
    }

    /**
     * 扣除血量
     * @return 实际扣除的血量
     */
    public int deincrementBlood(int num) {
        //血量必须是正数
        if (num <= 0) {
            throw new NException(NException.SERVICE_FIGHT_DEINCREMENT_BLOOD_ERROR);
        }

        //验证是否存活
        if (this.getBlood() <= 0) {
            return 0;
        }

        //攻击力大于当前实体则变攻击力为当前实体的血量
        if (num > this.getBlood()) {
            num = this.getBlood();
        }

        //攻击并计算剩余血量
        this.setBlood(this.getBlood() - num);

        //实体已经死亡
        if (this.getBlood() <= 0) {
            _dead();
        }

        return num;
    }

    /**
     * 当前实体是否存活
     * @return
     */
    public boolean isAlive() {
        if (this.blood <= 0) {
            return false;
        }
        return true;
    }

    /**
     * 内部死亡接口
     */
    private void _dead() {
        dead();
    }

    /**
     * 各实体需实现的死亡接口
     */
    public void dead() {


        setStatus(SEntityConstant.SENTITY_STATUS_DEAD);

        //调用死亡的handle
        SDeadInnerMessage deadMessage = new SDeadInnerMessage(this.eid);
        deadMessage.exeHandler();
    }

    /**
     * 更新方法
     */
    public void update(int diff) {
        //掉线与死亡实体不更新
        if (getStatus() != SEntityConstant.SENTITY_STATUS_NORMAL) {
            return;
        }

        //更新移动
        if (movement != null) {
            movement.moveUpdate(diff);
        }

        //更新AI
        if (aiEngine != null) {
            aiEngine.updateAI(diff);
        }

    }

    /**
     * 更新当前实体移动方向
     * @param dir
     * @return
     */
    public boolean updateMove(int dir) {
        //是否存活
        if (!isAlive()) {
            throw new NException(NException.SERVICE_FIGHT_ALREADY_DIED);
        }

        this.movement.setDir(dir);

        return true;
    }

    /**
     * 更新当前实体移动方向
     * @param dir
     * @param x             客户端的位置, 作为修正位置使用
     * @return
     */
    public boolean updateMove(int dir, int x) {
        //是否存活
        if (!isAlive()) {
            return false;
        }

        this.x = x;
        this.movement.setDir(dir);

        return true;
    }

    /**
     * AI初始化
     * @return
     */
    public abstract boolean initAI();

    /**
     * 返回AI
     * @return
     */
    public SUnitAI getAi() {
        return aiEngine;
    }

}
