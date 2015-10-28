package org.goldratio.event

import org.goldratio.game.player.Player

/**
 * Created by goldratio on 10/27/15.
 */
object EventHandler {

  sealed class PushCommand(
    val command: String,
    val module: String,
    val intro: String)

  object PushCommand {
    case object PUSH_UPDATE extends PushCommand("push@player", "update", "一般的推送消息")
    case object PUSH_BUILDING_OUTPUT extends PushCommand("push@building", "output", "建筑10s产出")
    case object PUSH_BUILDING_UPGRADE extends PushCommand("push@building", "upgrade", "建筑升级")
    case object PUSH_CHAT_SEND extends PushCommand("push@chat", "chatSend", "发送聊天信息")
    case object PUSH_BATTLE_DOBATTLE extends PushCommand("push@battle", "doBattle", "发送战斗信息")
    case object PUSH_GM_BATTLE_DOBATTLE extends PushCommand("push@battle", "doGmBattle", "gm发送战斗信息")
    case object PUSH_TASK extends PushCommand("push@task", "curTask", "任务信息更新")
    case object PUSH_GENERAL extends PushCommand("push@general", "general", "武将招募")
    case object PUSH_GENERAL_BATTLE extends PushCommand("push@generalBattle", "general", "武将损失兵力")
    case object PUSH_ANTIADDICTION extends PushCommand("push@antiaddiction", "antiaddiction", "防沉迷")
    case object PUSH_NOTICE extends PushCommand("push@notice", "notice", "醒目播报")
    case object PUSH_ATTMOV extends PushCommand("push@cities", "attmov", "世界里的移动变化")
    case object PUSH_CITIES extends PushCommand("push@cities", "cities", "世界信息变化")
    case object PUSH_MANWANG extends PushCommand("push@cities", "manwang", "蛮王令城信息")
    case object PUSH_POLITICS_EVENT extends PushCommand("push@politics", "politics", "政务系统事件")
    case object PUSH_OFFICER_BUILDING_APPLY extends PushCommand("push@officerBuildingApply", "officerBuilding", "新官职建筑申请")
    case object PUSH_AUTO_POWER extends PushCommand("push@autoPower", "autoPower", "自动副本次数")
    case object PUSH_BATTLE_REWARD extends PushCommand("push@batReward", "batReward", "战斗奖励")
    case object PUSH_KFWD_MATCH extends PushCommand("push@kfwdMatch", "kfwdMatch", "跨服武斗")
    case object PUSH_KFWD_MATCH_REPORT extends PushCommand("push@kfwdMatchReport", "kfwdMatchReport", "跨服武斗战报")
    case object PUSH_KFGZ_MATCH extends PushCommand("push@kfgzMatch", "kfgzMatch", "跨服国战")
    case object PUSH_PLAYERINFO extends PushCommand("push@getPlayerInfo", "info", "玩家重新登录")
    case object PUSH_TRICKINFO extends PushCommand("push@trickInfo", "trickInfo", "玩家受到计策伤害")
    case object PUSH_SLAVE extends PushCommand("push@slave", "slaveInfo", "奴隶系统信息")
    case object PUSH_STORE extends PushCommand("push@store", "refresh", "强制刷新商店")
    case object PUSH_WINDOW extends PushCommand("push@window", "window", "推送弹窗消息")
    case object PUSH_CITY_MESSAGE extends PushCommand("push@citymessage", "message", "推送城市消息列表")
    case object PUSH_WORLD extends PushCommand("push@world", "refresh", "刷新世界")
    case object PUSH_POWER extends PushCommand("push@power", "power", "刷新副本")
    case object PUSH_WORLD_CNP extends PushCommand("push@world", "cnpInfo", "世界国力值")
    case object PUSH_RIGHT_NOTICE extends PushCommand("push@rightNotice", "rightNotice", "右侧消息播报")
    case object PUSH_REDIRECT_URL extends PushCommand("push@redirectUrl", "message", "重新跳转")
    case object PUSH_WORLD_REWARD extends PushCommand("push@worldReward", "worldReward", "整点发送军资信息")
    case object PUSH_KILL_ADD extends PushCommand("push@worldKillChange", "worldKill", "击杀数变化")
    case object PUSH_GENERAL_INFO extends PushCommand("push@generalInfo", "youDiChuji", "武将条诱敌出击信息")
    case object PUSH_GENERAL_INFO2 extends PushCommand("push@generalInfo", "onQueues", "武将上阵信息")
    case object PUSH_GENERAL_INFO3 extends PushCommand("push@generalInfo", "teamOrder", "武将被加速补兵")
    case object PUSH_GENERALMOVE extends PushCommand("push@cities", "generalMove", "武将移动")
    case object PUSH_OFFICER_TOKEN extends PushCommand("push@officerToken", "tokenInfo", "当前官员令消息")
    case object PUSH_GOLD_ORDER_BATTLE extends PushCommand("push@goldOrderBattle", "goldOrderInfoBattle", "战斗中的玩家是否可以再发征召令")
    case object PUSH_NATION_TASK_STATE_CHANGE extends PushCommand("push@nationTaskStateChange", "taskMessage", "国家任务消息")
    case object PUSH_BAR_TASK_STATE_CHANGE extends PushCommand("push@barTaskStateChange", "bartaskMessage", "国家任务消息")
    case object PUSH_NATION_TASK_SIMPLE extends PushCommand("push@nationTask", "simpleMessage", "国家任务消息")
    case object PUSH_TEAM_FULL extends PushCommand("push@teamInfo", "full", "军团已满员")
    case object PUSH_TEAM_CHANGE extends PushCommand("push@teamInfo", "info", "军团成员变更")
    case object PUSH_TEAM_REWARD extends PushCommand("push@teamInfo", "reward", "获得奖励")
    case object PUSH_TEAM_GENERAL_BAT extends PushCommand("push@teamInfo", "teamBat", "集团军出阵")
    case object PUSH_TEAM_START_BAT extends PushCommand("push@teamInfo", "joinInfo", "集团军出阵战场公告信息")
    case object PUSH_BARBARAIN_INVADE extends PushCommand("push@barbarain_invade", "invade_info", "蛮族入侵消息")
    case object PUSH_BARBARAIN_FADONG extends PushCommand("push@barbarain_fadong", "fadong_info", "蛮族发动消息")
    case object PUSH_CITY_EVENT extends PushCommand("push@cityEvent", "cityEventInfo", "城池事件消息")
    case object PUSH_PLAYER_EVENT extends PushCommand("push@playerEvent", "playerEventInfo", "玩家城池事件消息")
    case object PUSH_BARBARAIN_INVADE_FOOD_ARMY extends PushCommand("push@barbarainInvadeFoodArmy", "foodArmyInfo", "蛮族发动消息")
    case object PUSH_GENERAL_JUBENMOVE extends PushCommand("push@juben", "generalMove", "剧本武将移动")
    case object PUSH_ATTMOV_JUBEN extends PushCommand("push@juben", "attmov", "剧本里的移动变化")
    case object PUSH_JUBEN extends PushCommand("push@juben", "refresh", "刷新剧本")
    case object PUSH_JUBEN_INFO extends PushCommand("push@juben", "info", "刷新剧本界面")
    case object PUSH_JUBEN_RES extends PushCommand("push@juben", "res", "剧本结果")
    case object PUSH_JUBEN_FLAG extends PushCommand("push@juben", "flag", "推送剧本flag")
    case object PUSH_WHOLE_KILL extends PushCommand("push@wholeKill", "wholeKill", "推送是否有整点杀敌")
    case object PUSH_JUBEN_DIALOG extends PushCommand("push@juben", "dialog", "推送剧本dialog")
    case object PUSH_JUBEN_STATE extends PushCommand("push@juben", "bat", "刷新剧本战斗状态")
    case object PUSH_JUBEN_TIMECHANGE extends PushCommand("push@juben", "timeChange", "剧本结束时间发生变化")
    case object PUSH_JUBEN_NPCTRICK extends PushCommand("push@juben", "npcTrick", "事件中npc施放计策")
    case object PUSH_JUBEN_EVENTFINISH extends PushCommand("push@juben", "eventFinish", "剧本中事件完成,用于成就的推送")
    case object PUSH_JUBEN_EVENTOVER extends PushCommand("push@juben", "eventOver", "剧本事件完成,用于城市上头像的消失")
    case object PUSH_JUBEN_GENERALADD extends PushCommand("push@juben", "generalAdd", "剧本增加武将推送信息")
    case object PUSH_ALL_TRICK extends PushCommand("push@juben", "allTrick", "全屏计策倒计时")
    case object PUSH_EVENT_DEADLINE extends PushCommand("push@juben", "eventDeadLine", "事件结束倒计时")
    case object PUSH_MAN_WANG_LING extends PushCommand("push@manWangLing", "manWangLing", "蛮王令消息")
    case object PUSH_REPUTAION extends PushCommand("push@reputation", "reputation", "推送官威信息")
    case object PUSH_CHASING_INFO extends PushCommand("push@juben", "chasingInfo", "追逐信息变化时推送")
    case object PUSH_ZERO_OCLOCK_RESET_REFRESH extends PushCommand("push@zeroOclockReset", "refreshInfo", "凌晨重置，前端刷新")
    case object PUSH_WIZARD_WORKSHOP extends PushCommand("push@wizardWorkShop", "Info", "术士工坊ICON提示")
    case object PUSH_FB_GUIDE extends PushCommand("push@fubenGuide", "Info", "副本引导")
    case object PUSH_JUBEN_ROAD_LINKED extends PushCommand("push@roadLinked", "road", "剧本内道路连通与否信息")
    case object PUSH_JUBEN_MARCHING_INFO extends PushCommand("push@juben", "marching", "剧本里行军事件信息")
    case object PUSH_COURTESY_EVENT extends PushCommand("push@courtesy_event", "courtesyEvent", "礼尚往来")
    case object PUSH_HJ_REWARD extends PushCommand("push@nationTask", "hjReward", "黄巾任务占城奖励")
    case object PUSH_NATIONMIRACLE extends PushCommand("push@nationTask", "miracleInfo", "国家奇迹建造信息")
    case object PUSH_NATIONMIRAL_WORKERINFO extends PushCommand("push@nationTask", "workerInfo", "国家奇迹运输队运粮队信息")
    case object PUSH_JUBEN_ROYALJADE_TRANSFER extends PushCommand("push@juben", "transferJade", "剧本里玉玺的转移信息")
    case object PUSH_HUIZHAN_BATTLE_LETTER extends PushCommand("push@huizhan", "hzBattleLetterIcon", "宣战成功后向前端推送战书")
    case object PUSH_HUIZHAN_GATHER extends PushCommand("push@huizhan", "hzGatherIcon", "宣战进入准备阶段，推送召集图标")
    case object PUSH_HUIZHAN_PK_REWARD extends PushCommand("push@huizhan", "hzPkReward", "会战单挑奖励")
    case object PUSH_HUIZHAN_FORCE_CHANGE extends PushCommand("push@huizhan", "hzForceChange", "会战双方兵力变更")
    case object PUSH_HUIZHAN_TASK_INFO extends PushCommand("push@huizhan", "hzTaskInfo", "会战任务栏信息")
    case object PUSH_HUIZHAN_INFO_IN_CITY extends PushCommand("push@huizhan", "hzInfoInCity", "会战在世界城市中的表现信息")
    case object PUSH_HUIZHAN_ICON extends PushCommand("push@huizhan", "HuiZhanIcon", "会战图标")
    case object PUSH_HUIZHAN_SUPPORT_TOKEN_ICON extends PushCommand("push@huizhan", "hzSupportTokenIcon", "会战第三国支援令图标")
    case object PUSH_JUBEN_MENGDEINFO extends PushCommand("push@juben", "mengdeInfo", "推送曹军地点变换")
    case object PUSH_KFZB extends PushCommand("push@kfzb", "report", "战报信息")
    case object PUSH_JUBEN_CITY_CHANGE extends PushCommand("push@juben", "cityChange", "推送城市外观变化信息")
    case object PUSH_WORLD_DRAMA extends PushCommand("push@worlddrama", "missionComplete", "一轮科技对应的剧本全部完成")
    case object PUSH_WORLD_FARM extends PushCommand("push@worldfarm", "worldfarm", "推送屯田信息")
    case object PUSH_NATIONINDIV_TASK extends PushCommand("push@indiv", "procChange", "推送个人任务进度变化信息")
    case object PUSH_AUTO_BATTLE extends PushCommand("push@autoBattle", "autoBattle", "推送自动国战变化信息")
    case object PUSH_NATIONINDIV_REWARD extends PushCommand("push@indiv", "taskComplete", "推送个人任务完成时奖励相关信息")
  }

  def handle(playerId: Int, sessionPlayer: Player, command: PushCommand): Unit = {
    EventListener.getEvent(playerId).map {
      case (eventId, event) =>

    }
  }

}
