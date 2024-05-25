# Mahjong

庄家第一轮的特殊性：庄家开局直接弃牌

每轮中逻辑的修正：玩家回合中只有摸牌和弃牌操作，弃牌后系统自动识别其他玩家是否有机会执行碰或杠操作，检测后询问用户是否执行，对于存在多种选择的情况，邀请用户做出选择。

Todo: 癞子未实现

缺陷：输出页面表述不清晰，信息不便于观察。但这个不重要，UI界面每次循环都应该做出牌堆刷新。

# 2024/4/26
提交人：刘乐

Hand类： 修改碰和杠操作。 根据弃牌堆的最后一张牌执行碰和杠

Player类： 根据完善类图的要求丰富玩家功能。玩家操作：弃牌，抓牌， 碰，吃，杠。（吃未实现）。增加玩家类型东南西北，取代玩家名称。

TileStack类：向牌堆种加入癞子牌，总牌数达到140张。功能并未实现

# 2024/4/27
提交人：王子恒

CheckTile：加入吃的方法，更改部分代码逻辑

Hand：完善吃的判断逻辑，加入已经形成的牌型列表

Player：加入下一名玩家的判断，目的是对吃的操作增加限制

PlayerType: 加入判断下一名玩家的方法

Tile: 加入赖子属性, 赖子为万 条 桶 东南西北 中发白中的一种，而不是新的牌型

Game: 优化玩家出牌判断顺序，执行杠、碰操作会跳过中间玩家，完善初始化赖子牌的功能

增加UI中

缺点：只添加了部分赖子的方法，缺少对赖子判断（杠，碰，吃，赢）的操作，修改的时候不管是胡牌还是
进行吃碰操作的时候都要根据代码的复杂性来判断，可建立新的方法，也可以直接在原有的代码上修改
具体的可以在群中讨论

# 2024/5/2
提交人：王子恒

增加View包，主要负责管理UI界面

添加Resource包，主要负责相关的图片

# 2024/5/3
提交人：王子恒

新增碰、杠和吃按钮，吃的按钮是否应该存在还在思考，增加在手牌进行碰和杠的逻辑

缺少玩家出牌后对其他玩家进行测试的逻辑，稍后添加

缺少：赖子逻辑、得分系统和开始规则介绍界面和决定地主界面

中招了，有点烧，明日再更

# 2024/5/5
提交人：王子恒

删除吃(Chow)按钮，不允许玩家在自己轮次进行吃的操作，只能在上一家出牌的时候执行吃操作

增加杠 碰后的牌型展示，放在手牌的右侧，距离手牌大约半个牌的距离

还在更新中

# 2024/5/6
提交人：王子恒

增加当前玩家打出牌后对于其他玩家的判定功能，由于时间问题只完成碰操作，其他操作明日会完成

缺少：赖子逻辑、得分系统和开始规则介绍

# 2024/5/7
提交人：王子恒

Hand类：修复Sort方法只排手牌的问题，加入参数让其能够对传入参数序列进行排序

GameUI: 加入对当前玩家出牌的吃 杠逻辑判断。

PreparationUI: 增加欢迎界面，主要的目的是让玩家投骰子来判断庄家

缺少：赖子逻辑、得分逻辑

# 2024/5/17
提交人：王子恒

GameUI: 修复图片显示延迟问题。对已经组成的牌区向右增加一个显示位置，以避免重叠问题。

Hand类：增加发牌过程， 增加玩家可以自由选择牌型顺序，只限于 五种类型牌的摆放

CheckTile: 修复吃操作的逻辑问题，修复常规胡牌算法的检测问题

增加test类以检测部分算法

修复其他类一些已知问题

# 2024/5/18
提交人： 刘乐
合并dev devUI分支
拉 repaint GameUI


# 2024/5/18
提交人：王子恒

GameUI: 修复无牌和有玩家获胜的显示

CheckTile: 修复findPair逻辑

修复部分已知问题

# 2024/5/19
提交人： 刘乐

后端代码整合，将框架优化，增加了一些注释，删除了一些无用的代码。

修改封装性和调用的合理性。如在游戏中总是调用player类中的操作方法，而不是直接操作手牌或牌堆。

# 2024/5/22
提交人： 刘乐
GameUI 代码重构整合。 paint方法重构

按钮优化

尽量将GameUI中仅调用Game类的方法，优化方法的封装性。

# 2024/5/23
待修改，吃的逻辑问题。打出4，不能用3，5吃

已修改，玩家操作逻辑，调整调用game中的方法，调整封装性。

# 2024/5/25
提交人： 刘乐

针对Game, GameUI, Player, Hand, CheckTile. 代码组织重新架构，按照类图重新设计代码结构。

Game和GameUI的耦合度降低，GameUI只调用Game中的方法。（并不完善，之后改动也需要遵循这一逻辑。）

Game中仅调用Player类的方法，Player类中仅调用Hand类的方法，Hand类中仅调用CheckTile类的方法。

尽力是代码更加清晰，便于维护。

目前显然的问题已经用TODO标注，后续请优先解决TODO问题。

牌堆耗尽游戏不会结束的问题（未用TODO标注）仍待解决。

周一的例会上我会介绍一下代码的整体结构，以及后续的开发计划。

