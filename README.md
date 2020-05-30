# ganker
一個能煩死別人的小程式  
能從遠端朝惡作劇目標電腦定時放音樂，或執行cmd

## 各檔用途
* `ganker.java`  
一個沒有gui的java程式，監聽預設port`8787`來執行命令，port可由執行參數中修改  
**執行時需於jar檔案路徑`/music/`內放入名為`music`的`wav`格式聲音檔，作為gank時播放使用**
* `gankerController.java`  
控制`ganker`用的程式，可以下達`ganker命令`，將於後方說明  
啟動時預設連線`localhost:8787`，可於執行參數中修改
* `gankerControllerController.java`  
控制`gankerController`用的程式，可掃描目前同區域網路下的`ganker`，並開啟對應的`gankerController`  
啟動時預設掃描port`8787`，可於執行參數中修改  
**此檔案編譯時需要用classpath連結`gankerController.java`**

## ganker命令
* `整數`  
將於`整數`毫秒後開始播放音樂
* `cmd:指令`  
將執行`指令`內容，如果在windows下將於cmd內執行  
**注意：有些命令不被支援，可以嘗試呼叫新的cmd後再執行不支援的命令**
* `stop`  
停止播放音樂
* `exit`
結束`ganker`程式

## 使用方法
在要惡作劇的目標電腦上執行`ganker.jar`，或是更派一點，把`ganker.jar`放到開機自動啟動資料夾  
再在同區域網路內其他台電腦（或是惡作劇目標本身也行）上執行`gankerController.jar`，如果不在惡作劇目標電腦上執行，記得在執行時給予`gankerController`IP參數，指向惡作劇目標，如果`ganker`的port設定不是預設的`8787`，記得也要改`gankerController`的第二個參數  
然後就可以輸入`ganker命令`了  
如果不知道惡作劇目標在區域網路的哪個IP，可以使用`gankerControllerController`來尋找

# 詳細使用方式
## ganker.jar
命令列：`java -jar ganker.jar [port]`  
`port`預設為`8787`  

## gankerController.jar
命令列：`java -jar gankerController.jar [ip] [port]`  
`ip`預設為`localhost`  
`port`預設為`8787`  
GUI操作：
* 左上文字框：指令輸入
* 右上文字框：當左邊輸入數字時，將會乘以右邊的倍率送出（預設倍率為毫秒->分鐘）
* send按鈕：送出命令
* stop按鈕：快速鍵，送出停止播放命令
* check按鈕：檢查目標`ganker`是否還活著
* exit按鈕：快速鍵，送出關閉`ganker`命令

## gankerControllerController.jar
命令列：`java -jar gankerControllerController.jar [port]`  
`port`預設為`8787`
GUI操作：
* 選項`reload`  
重新掃描區域網路下的`ganker`
* 選項`ip:port`  
開啟相對的`gankerController`

# 應用方式
基本用法：可以把惡作劇目標電腦開滿計算機，或是在使用到一半開始抬棺等等
進階用法：用taskkill指令砍掉正在運作的程式
超強用法：呼叫powershell下載檔案，也可以用這方法遠端更新`ganker`

# 警語
不要玩太過頭喔


