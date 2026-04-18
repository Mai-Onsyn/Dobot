# **DobotE6V4 Kotlin 控制代码**

要运行操作控制台，执行src/main/kotlin/cius/mai_onsyn/dobot/Main.kt；

或：
- mvn package
- java -jar ./target/Dobot-1.0-SNAPSHOT.jar

**以下为控制台所有命令解释**

## 基本配置(bot)
| 命令 | 功能描述 | 示例 |
| :---- | :---- | :---- |
| **enable** | 机械臂使能。 | bot enable |
| **disable** | 机械臂下使能。 | bot disable |
| **connect** | TCP连接机械臂。 | bot connect |
| **disconnect** | 与机械臂断开连接。 | bot disconnect |
| **cls** | 清空报警。 | bot cls |
| **handc** | Modbus连接手。 | bot handc |
| **handd** | 断开手的Modbus连接。 | bot handd |
| **setup** | 初始化机械臂，以便快速使用，执行包含清空报警，请求TCP控制，停止当前动作，Modbus连接手。 | bot setup |
| **startdrag** | 开启机械臂关节可拖拽模式。 | bot startdrag |
| **stopdrag** | 关闭机械臂关节可拖拽模式。 | bot stopdrag |


## **机械臂运动控制**

这类命令用于直接控制 Dobot E6V4 机械臂的关节运动。

| 命令 | 功能描述 | 示例 |
| :---- | :---- | :---- |
| **movj** | 关节绝对运动。支持全参数设置或局部修改。 | movj 0 0 90 0 0 0 |
| **movjog** | 关节增量运动。在当前角度基础上进行加减。 | movjog j1 10 j3 \-5 |

### **详细说明：**

* **movj 全参数模式**: 依次输入 6 个关节的角度。  
  * movj 10 20 30 40 50 60  
* **movj 局部修改模式**: 仅修改指定的关节，其余保持当前姿态。  
  * movj j1 45 (仅将 J1 旋转至 45 度)  
* **movjog 增量模式**: 使用 j1\~j6 关键字配合数值。  
  * movjog j2 5.5 (J2 关节在当前基础上增加 5.5 度)

## **手控制**

用于控制机械臂末端搭载的灵巧手。范围均为[0,255]，255为完全展开。

### **finger \- 单指/精确控制**

设置单根手指或所有关节的姿态。

* **全关节设置**: 输入 7 个整数参数。  
  * 示例: finger 100 100 100 100 100 100 100  
* **大拇指特殊控制**: finger 1 \<pitch|yaw|roll\> \<数值\>  
  * 示例: finger 1 pitch 50  
* **指定关节修改**: finger \<索引\> \<数值\>  
  * 示例: finger 3 150

### **hand \- 四指同步控制**

快速设置除大拇指以外的 4 根手指（食指、中指、无名指、小指）到相同位置。

* **用法**: hand \<数值\>  
* 示例: hand 255 (四根手指全部展开)

## **泵控制(pump)**

用于控制外接泵设备的操作。

| 子命令 | 功能描述 | 示例 |
| :---- | :---- | :---- |
| **connect** | 自动扫描并连接串口泵 | pump connect |
| **status** | 查看泵当前状态（电机、速度、位置等） | pump status |
| **enable** | 使能电机（锁定） | pump enable |
| **disable** | 释放电机（可手动转动） | pump disable |
| **stop** | 紧急停止当前抽水动作 | pump stop |
| **speed** | 设置最大转速 | pump speed 1500 |
| **accel** | 设置加速度 | pump accel 500 |
| **\<数字\>** | 抽水指定体积（ml） | pump 20 |

## **轨迹记录与回放(record)**

用于记录一系列关节序列并保存为文件，以便后续回放。

* **record**: 记录机械臂当前的关节位置到内存序列中。  
* **record clear**: 清空当前内存中已记录的所有点位。  
* **record list**: 列出当前内存中记录的所有点。
* **record save \<文件名\>**: 将内存中的序列保存为本地 JSON 文件。  
  * 示例: record save "pick\_and\_place"  
* **record replay \<文件名\>**: 加载本地 JSON 轨迹文件并驱动机械臂执行。  
  * 示例: record replay "pick\_and\_place"
* **record load \<文件名\>**: 加载本地JSON轨迹文件到内存中。
  * 示例: record load "pick\_and\_place"

## 机械臂恢复(resume)

用于将机械臂和手移动到指定位置。

- **resume init**: 机械臂竖直，手完全张开。
- **resume pose**: 机械臂为包装纸箱上的姿势，手摆出国际有好手势。

## **程序控制**

控制软件的命令。

- **exit**: 退出控制台