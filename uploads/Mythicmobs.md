实体目标选择器

单实体

@Self | 选取[施法者]自身
例子，当收到伤害时，向自身发送消息：- m{m="&a你好"} @Self ~onDamaged

@Target | [施法者]的当前目标（并非技能目标）
例子，当收到伤害时，向目标发送消息：- m{m="&a你好"} @Target ~onDamaged

@Trigger（默认目标器）| 激活[触发器]的实体
例子，当收到伤害时，向此技能的触发者发送消息：- m{m="&a你好"} @Trigger ~onDamaged

@NearestPlayer | 指定方块格数半径的球体范围内的最近玩家（中心: [施法者]）| 修改项：[Radius(R)—球体半径;]
例子，当收到伤害时，向球体半径5格内最近的玩家发送消息：- m{m="&a你好"} @NearestPlayer{r=5} ~onDamaged

@WolfOwner | 狼的驯服者 
例子，当收到伤害时，向驯服者发送消息：- m{m="&a救救我"} @WolfOwner ~onDamaged

@Owner | 施法者的主人（比如怪物A召唤了怪物B，那么怪物A就是怪物B的主人）
例子，当收到伤害时，向主人发送消息：- m{m="&a主人，救我"} @Owner ~onDamaged

@Parent | 父系实体（比如怪物A召唤了怪物B，那么怪物A就是怪物B的父系实体）
例子，当收到伤害时，向父系实体发送消息：- m{m="&a爹，救我"} @Parent ~onDamaged

@Mount | [施法者] (非玩家)所设定好的坐骑（取决于mount技能所选定的第一个实体，其余的不会选中）
例子，当收到伤害时，向设定好的坐骑发送消息：- m{m="&a快跑啊！"} @Mount ~onDamaged

@Vehicle | [施法者]当前坐骑
例子，当收到伤害时，向当前的坐骑发送消息：- m{m="&a快跑啊！"} @Vehicle ~onDamaged

@Passenger | [施法者]的骑乘者
例子，当收到伤害时，向当前的骑乘者发送消息：- m{m="&a哥！我要死了，快去别的地方！"} @Passenger ~onDamaged

@PlayerByName | 指定名称的玩家（必须要有修改项）| 修改项：[Name(N)—玩家名(支持占位符);]
例子，当收到伤害时，向指定的玩家(test)发送消息：- m{m="&a玩家，你好！"} @PlayerByName{n=test} ~onDamaged

@UUID | 指定字符串的实体（必须要有修改项）| 修改项：[UUID(U)—UUID字符串(支持占位符);]
例子，当收到伤害时，向指定的实体发送消息：- m{m="&a实体，你好！"} @UUID{U=29854700-71fb-44d3-a8b6-e6bd40d1c70c} ~onDamaged



多实体

球体范围

@IIR | 指定方块格数半径的球体范围内的所有掉落物（中心: 玩家）| 修改项：[Radius(R)—球体半径;]
例子，当收到伤害时，向半径5格内的所有掉落物发送消息：- m{m="&a都是玩家周围的掉落物。"} @IIR{r=5} ~onDamaged

@INO | 指定方块格数半径的球体范围内的所有掉落物（中心: 坐标原点）| 修改项：[Radius(R)—球体半径;]
例子，当收到伤害时，向半径5格内的所有掉落物发送消息：- m{m="&a都是原点周围的掉落物。"} @INO{r=5} ~onDamaged

@EIR | 指定方块格数半径的球体范围内的所有实体（中心: 施法者）| 修改项：[Radius(R)—球体半径;LivingOnly(Living,L)—是否仅选取生物（箭, 盔甲架, 矿车等不属于生物]
例子，当收到伤害时，向半径5格内的所有生物实体发送消息：- m{m="&a都是施法者周围的生物实体。"} @INO{r=5,l=true} ~onDamaged

@ENO | 指定方块格数半径的球体范围内的所有实体（中心: 坐标原点）| 修改项：[Radius(R)—球体半径;LivingOnly(Living,L)—是否仅选取生物（箭, 盔甲架, 矿车等不属于生物]
例子，当收到伤害时，向半径5格内的所有生物实体发送消息：- m{m="&a都是坐标原点周围的生物实体。"} @INO{r=5,living=true} ~onDamaged

@NLNO | 指定方块格数半径的球体范围内的所有无活性实体（中心: 坐标原点）| 修改项：[Radius(R)—球体半径;]
例子，当收到伤害时，向半径5格内的所有无活性实体发送消息：- m{m="&a都是坐标原点周围的无活性实体。"} @NLNO{r=5} ~onDamaged

@EIW | 所处世界的实体 | 修改项：[Radius(R)—球体半径;]
例子，当收到伤害时，向当前世界半径5格内的所有实体发送消息：- m{m="&a都是这个世界内的实体。"} @EIW{r=5} ~onDamaged

@ERNO | 多个指定方块格数半径的球体范围内的实体（中心: 以坐标原点为中心所成的圈上的每一点）| 修改项：[Radius(R)—球体半径;]
例子，当收到伤害时，在坐标原点半径5格内组成圆圈上的每一个点生成火焰粒子特效：- particle{p=flame;a=1} @ERNO{r=5} ~onDamaged

@MIR | 指定方块格数半径的球体范围内的指定多个实体（中心: 施法者）| 修改项：[Radius(R)—球体半径;Types(T)—所选取的实体ID或MM实体内部名;]
例子，当收到伤害时，向施法者半径5格内的所有指定实体（原版的僵尸(Zombie),MM实体(TestMob)）发送消息：- m{m="&a你们都是指定的实体之一。"} @MIR{r=5;t=Zombie,TestMob} ~onDamaged

@MobsNearOrigin | 指定方块格数半径的球体范围内的指定多个实体（中心: 坐标原点）| 修改项：[Radius(R)—球体半径;Types(T)—所选取的实体ID或MM实体内部名;]
例子，当收到伤害时，向坐标原点半径5格内的所有指定实体（原版的僵尸(Zombie),MM实体(TestMob)）发送消息：- m{m="&a你们都是指定的实体之一。"} @MobsNearOrigin{r=5;t=Zombie,TestMob} ~onDamaged

@PIR | 指定方块格数半径的球体范围内的玩家（中心: 施法者）| 修改项：[Radius(R)—球体半径;]
例子，当收到伤害时，向施法者半径5格内的所有玩家发送消息：- m{m="&a玩家，你好！"} @PIR{r=5} ~onDamaged

@PlayersNearOrigin | 指定方块格数半径的球体范围内的玩家（中心: 坐标原点）| 修改项：[Radius(R)—球体半径;]
例子，当收到伤害时，向坐标原点半径5格内的所有玩家发送消息：- m{m="&a玩家，你好！"} @PlayersNearOrigin{r=5} ~onDamaged



其他范围

@EIIR | 圆环内的实体（中心: 施法者）| 修改项：[MinRadius(Min)—内环半径;MaxRadius(Max)—外环半径]
例子，当收到伤害时，向施法者内环半径5格，外环半径10格的所有实体发送消息：- m{m="&a圆环内的实体，你好！"} @EIIR {min=5;max=10} ~onDamaged

@EIC | 底面朝前的圆锥体内的所有实体（顶点: 施法者）| 修改项：[Angle(A)—锥体的扇面角度（单位：角度）;Range(R)—锥体的母线长度（单位：格方块）;Rotation(Rot)—母线水平旋转角度，正左负右（单位：角度）]
例子，当收到伤害时，向施法者前方椎体角度为90度，母线长度为5格方块，母线水平旋转角度为90度的所有实体发送消息：- m{m="&a椎体内的实体，你好！"} @EIC{a=5;r=90;rot=90} ~onDamaged

@World | 所处世界内的玩家
例子，当收到伤害时，向当前世界内的所有玩家发送消息：- m{m="&a本世界内的玩家，你好！"} @World ~onDamaged

@Server | 所处服务器内的玩家
例子，当收到伤害时，向当前服务器内的所有玩家发送消息：- m{m="&a本服务器内的玩家，你好！"} @Server ~onDamaged

@PlayersInRing | 圆环内的玩家（中心:施法者）| 修改项：[MinRadius(Min)—内环半径;MaxRadius(Max)—外环半径]
例子，当收到伤害时，向施法者内环半径5格，外环半径10格的所有玩家发送消息：- m{m="&a圆环内的玩家，你好！"} @PlayersInRing{min=5;max=10} ~onDamaged

@Child | 选取子系实体（搭配技能Summon）
例子，当收到伤害时，向其使用Summon技能召唤出来的实体发送消息：- m{m="&a我的子系实体，你好！"} @Child ~onDamaged

@Sibling | 选取同内部名实体
例子，当收到伤害时，向相同内部名(假定当前怪物的内部名是TestZombie，则选择其他同样内部名一样的)的实体发送消息：- m{m="&a嘿，兄弟，你好！"} @Sibling ~onDamaged



威胁度 （需启用实体模块: 威胁度）

@rtt | 随机选取一个拥有威胁度的实体 
例子，当收到伤害时，向随机一个拥有威胁度的实体发送消息：- m{m="&a你被我选中了，危险的家伙！"} @rtt ~onDamaged

@tt | 选取所有拥有威胁度的实体
例子，当收到伤害时，向所有拥有威胁度的实体发送消息：- m{m="&a你们都对我有威胁！"} @tt ~onDamaged

@ttp | 选取所有拥有威胁度的玩家  
例子，当收到伤害时，向所有拥有威胁度的实体发送消息：- m{m="&a你们这些玩家都对我有威胁！"} @ttp ~onDamaged

@rttl | 随机选取一个拥有威胁度的实体的位置
例子，当收到伤害时，随机传送到一个拥有威胁度的实体位置：- teleport @rttl ~onDamaged



实体通用修改项（大部分情况下是配合ModelEngine使用的，一般使用中无需添加）

可用修改项 [UseboundingBox(bb)—是否选取碰撞箱;Unique(u)—单个实体可被选取多少个碰撞箱;NoMegBB(nmb)—是否无视MEG子碰撞箱]

此修改项属于接入到目标选择器中，如@Target{Unique=2}
例子，当收到伤害时，向选取了碰撞箱，且最大碰撞箱为10，并无视MEG子碰撞箱的目标造成10点伤害：- damage{a=10} @target{bb=true;u=10;nmb=true} ~onDamaged



坐标目标选择器

坐标目标选择器通用修改项（全部支持占位符,适用于后面的各类坐标选择器）

XOffset(XO,X) | 中心位置X轴偏移（单位: 格方块）
YOffset(YO,Y) | 中心位置Y轴偏移（单位: 格方块）
ZOffset(ZO,Z) | 中心位置Z轴偏移（单位: 格方块）
BlockCentered(Centered) | 是否选取方块中心                             
ForwardOffset(FOffset, FO) | 中心位置前后偏移（单位: 格方块 基于视角）
SideOffset(SOffset, SO) | 中心位置左右偏移（单位: 格方块 基于视角）
RotateX(RotX) | 准(基)线的X轴旋转角度
RotateY(RotY) | 准(基)线的Y轴旋转角度
RotateZ(RotZ) | 准(基)线的Z轴旋转角度
CoordinateX(CX) | 实际选择处的X轴坐标
CoordinateY(CY) | 实际选择处的Y轴坐标
CoordinateZ(CZ) | 实际选择处的Z轴坐标
CoordinateYaw(CYaw) | 实际选择处的视角水平旋转角度
CoordinatePitch(CPitch) | 实际选择处的视角俯仰视旋转角度
BlockTypes(BlockType,BT) | 仅选取的方块, 支持多个（格式: bt=A, B, C）
BlockIgnores(BlockIgnore,BI) | 所无视的方块, 支持多个（格式: bt=A, B, C）

例子，当收到伤害时，传送到施法者位置X轴偏移5格，Y轴偏移5格，Z轴偏移5格，且选取目标方块中心，前偏移为5格，左偏移为5格，实际选择处的视角俯仰视旋转角度为90，实际选择处的视角水平旋转角度为90，仅为stone与grass方块，忽视glass与diamond_block方块的位置：- teleport @SelfLocation{xo=5;yo=5;zo=5;centered=true;fo=5;so=5;cyaw=90;cpitch=90;bt=stone,grass;bi=glass,diamond_block}

单坐标

@SelfLocation | 施法者位置
例子，当收到伤害时，传送到施法者位置：- teleport @SelfLocation

@SelfEyeLocation | 选取施法者眼睛位置（通过定义通用修改项以偏移所选取位置）
例子，当收到伤害时，传送到施法者眼睛位置：- teleport @SelfEyeLocation ~onDamaged

@Forward | 视角前方第?格方块的位置 | 修改项：[Forward(F,Amount,A)—前方第几格位置的方块;Rotate(Rot)—视角准线的旋转角度;UseEyeDirection(UEL)—是否令基点位于实体眼部;LockPitch—是否忽视视角俯仰视角度]
例子，当收到伤害时，传送到施法者视角前方第5格方块，且基于眼部，并忽视视角俯仰视角度的位置：- teleport @Forward{f=5;uel=true;lockpitch=true} ~onDamaged

@TL | 仇恨目标位置 | 修改项：[MaxDistance(Max,Distance,D)—最远可选取到多远的方块;IgnoreTransparent(IT)—是否无视半透明方块]
例子，当收到伤害时，传送到施法者仇恨目标位置，且最远可选取到30格的目标，并无视半透明方块：- teleport @TL{d=30;it=true} ~onDamaged

@TargetBlock | 准心所瞄准着的方块
例子，当收到伤害时，传送到施法者准心所瞄准着的方块：- teleport @TargetBlock ~onDamaged

@TriggerLocation | 触发者位置
例子，当收到伤害时，传送到触发者位置：- teleport @TriggerLocation ~onDamaged

@Location | 指定坐标位置 | 修改项：[Location(Loc,L,C)—所选取位置的坐标（格式: X,Y,Z,视角水平旋转角度,视角俯仰视旋转角度）]
例子，当收到伤害时，传送到指定位置：- teleport @Location{l=0.0,0.0,0.0,90.0,180.0} ~onDamaged

@Origin | 坐标原点（搭配技能如Projectile使用）
例子，当抛射物行进时，在抛射物所在的坐标位置生成火焰粒子特效：- particle{p=flame;a=1} @Origin

@NearestStructure | 离施法者最近的结构（地牢等）的中心位置 | 修改项：[Type(T)—结构名]
例子，当收到伤害时，传送到离施法者最近的地牢结构：- teleport @NearestStructure{t=STRONGHOLD} ~onDamaged

@CasterSpawnLocation | 施法者的出生点
例子，当收到伤害时，传送到施法者的出生点：- teleport @CasterSpawnLocation ~onDamaged

@SpawnLocation | 所处世界的世界重生点
例子，当收到伤害时，传送到所处世界的世界重生点：- teleport @SpawnLocation ~onDamaged

@VariableLocation | 变量值所指定的位置 | 修改项：[Var(Name,N,Variable,Key,K)—所读取的变量名]
例子，当收到伤害时，传送到变量值所指定的位置：- teleport @VariableLocation{var=testLoc}  ~onDamaged



多坐标

@ForwardWall | 面前的一个立方体区域内的多个点（基点: 施法者）| 修改项：[Forward(F,Amount,A)—立方体的前后延伸;YOffset(Y)—立方体中心的Y轴偏移;Height(H)—立方体的上下延伸;Width(W)—立方体的左右延伸]
例子，当收到伤害时，在施法者面前一个前方延伸5格，上延伸2格，左延伸3格的立方体内的多个点生成火焰粒子特效：- particle{p=flame;a=1} @ForwardWall{f=5;h=2;w=3} ~onDamaged

@Cube | 绘制立方体并选取体内的每一点（基点: 技能目标）| 修改项：[X—立方体的前后延伸;Y—立方体中心的Y轴偏移;Z—立方体的上下延伸;Points(P,Density,D)—立方体边上均匀地选取多少个点,该值可充当点的密度;Fill(F)—是否选取体内,而非仅取体边上;OutLine(OnlyOutline,O,Edge,OnlyEdge,E)—是否仅选取体边上的点;FromOrigin(Origin)—基点是否为坐标原点;Rotation(R)—所绘制立方体的三轴旋转,格式:r=X,Y,Z]
例子，当收到伤害时，在技能目标处绘制立方体并选取多个点生成火焰粒子特效：- particle{p=flame;a=1} @Cube{x=3;y=3;z=3;p=100;fill=true} ~onDamaged

@Cone | 底面朝前的圆锥体内的多个点（顶点: 施法者）| 修改项：[Angle(A)—锥体的扇面角度;Range(R)—锥体的母线长度;Points(P)—所选取的最大点数量;MinPoints(MP)—所选取的最小点数量;Rotation(Rot)—母线水平旋转角度,正左负右;YOffset(YO,Y)—圆锥整体的Y轴偏移]
例子，当收到伤害时，在以施法者为顶点底面朝前的圆锥体内的多个点生成火焰粒子特效：- particle{p=flame;a=1} @Cone{a=180;r=5;p=100;mp=50} ~onDamaged

@Ring | 指定半径的圆上的多个点（中心: 施法者）| 修改项：[Radius(R)—圆半径;Points(P)—圆上平均选区的点数量;RotationX(Rotx,Rx)—圆整体的X轴旋转;RotationY(Roty,Ry)—圆整体的Y轴旋转;RotationZ(Rotz,Rz)—圆整体的Z轴旋转;OffsetX(Offx,Ox)—圆中心的X轴偏移;OffsetY(Offy,Oy)—圆中心的Y轴偏移;OffsetZ(Offz,Oz)—圆中心的Z轴偏移;Releative—三轴旋转是否基于视角]
例子，当收到伤害时，在以施法者为中心半径为5的平面圆上的多个点生成火焰粒子特效：- particle{p=flame;a=1} @Ring{r=5;p=50} ~onDamaged

@RAO | 指定半径的圆上的多个点（中心: 坐标原点）| 修改项：[Radius(R)—圆半径;Points(P)—圆上平均选区的点数量;RotationX(Rotx,Rx)—圆整体的X轴旋转;RotationY(Roty,Ry)—圆整体的Y轴旋转;RotationZ(Rotz,Rz)—圆整体的Z轴旋转;OffsetX(Offx,Ox)—圆中心的X轴偏移;OffsetY(Offy,Oy)—圆中心的Y轴偏移;OffsetZ(Offz,Oz)—圆中心的Z轴偏移;Releative—三轴旋转是否基于视角]
例子，当收到伤害时，在以坐标原点为中心半径为5的平面圆上的多个点生成火焰粒子特效：- particle{p=flame;a=1} @RAO{r=5;p=50} ~onDamaged

@RandomRingPoint | 指定半径的圆上的多个随机点（中心: 施法者）
例子，当收到伤害时，在以坐标原点为中心半径为5的平面圆上的多个随机点生成火焰粒子特效：- particle{p=flame;a=1} @RandomRingPoint{r=5} ~onDamaged

@RLNC | 指定内外环半径的圆环内的多个点（中心: 施法者）| 修改项：[Amount(A)—所取点数量;MaxRadius(Radius,R,MaxR)—外环半径;MinRadius(MinR)—内环半径;Spacing(S)—各点最小间隔;OnSurface—是否仅在地表上选取点]
例子，当收到伤害时，在以施法者为中心的平面圆环上的多个点生成火焰粒子特效：- particle{p=flame;a=1} @RLNC{maxr=5;minr=3;s=1} ~onDamaged

@Sphere | 指定方块格数半径的球体范围上的多个点（基点: 施法者）| 修改项：[Radius(R)—球体半径;Points(P)—球上点数]
例子，当收到伤害时，在以施法者为中心的球体上的多个点生成火焰粒子特效：- particle{p=flame;a=1} @Sphere{r=5;p=100} ~onDamaged

@BIR | 指定方块格数半径的球体范围内的指定方块（中心: 施法者）| 修改项：[Radius(R)—所指定水平半径;RadiusY(RY,YRadius,YR)—所指定垂直半径;Noise(N)—随机化程度;Shape(S)—范围形状（Sphere为球形,Cube为正方体）;NoAir(NA)—是否忽略空气;OnlyAir(OA)—是否仅选取空气;NearOrigin(NO)—是否优先选取距坐标原点较近的方块]
例子，当收到伤害时，在以施法者为中心的指定方块格数半径的球体范围内的指定方块上生成火焰粒子特效：- particle{p=flame;a=1} @BIR{r=5;ry=5;s=sphere} ~onDamaged

@BNO | 指定方块格数半径的球体范围内的指定方块（中心: 坐标原点）| 修改项：[Radius(R)—所指定水平半径;RadiusY(RY,YRadius,YR)—所指定垂直半径;Noise(N)—随机化程度;Shape(S)—范围形状（Sphere为球形,Cube为正方体）;NoAir(NA)—是否忽略空气;OnlyAir(OA)—是否仅选取空气;NearOrigin(NO)—是否优先选取距坐标原点较近的方块]
例子，当收到伤害时，在以坐标原点为中心的指定方块格数半径的球体范围内的指定方块上生成火焰粒子特效：- particle{p=flame;a=1} @BNO{r=5;ry=5;s=sphere} ~onDamaged

@BIC | 所处区块内的所有方块
例子，当收到伤害时，在所处区块内的所有方块上生成火焰粒子特效：- particle{p=flame;a=1} @BIC ~onDamaged

@Region | 指定两顶角所形成区域内的所有方块
例子，当收到伤害时，在指定两顶角所形成区域内的所有方块上生成火焰粒子特效：- particle{p=flame;a=1} @Region ~onDamaged

@Spawners | 指定多个生成点的位置
例子，当收到伤害时，在指定多个生成点的位置上生成火焰粒子特效：- particle{p=flame;a=1} @Spawners ~onDamaged

@Pin | 指定多个坐标点 | 修改项：[Pin(P,Name,N,Key,K)—坐标点名,多个","隔开]
例子，当收到伤害时，在指定多个坐标点的位置上生成火焰粒子特效：- particle{p=flame;a=1} @Pin ~onDamaged



无用目标选择器

@none | 无
例子，收到伤害时，不选取任何目标，模拟后台身份执行指令：- command{c=say 我是后台} @none ~onDamaged
