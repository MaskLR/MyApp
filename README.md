# 项目结构

## 根包
`com.lyy.myapp`  
项目的根包，包含所有代码。

---

## 数据层 (`data`)
负责数据的获取、存储及管理。

### 子模块
- **`local`**  
  本地数据存储模块。

- **`model`**  
  数据模型模块，定义数据结构。

- **`network`**  
  网络请求模块：
  - `ApiService.kt`：定义网络请求接口。
  - `NetworkClient.kt`：封装网络请求客户端。

- **`remote`**  
  远程数据源模块，用于与后端交互。

- **`repository`**  
  数据仓库模块，统一处理数据源逻辑（远程和本地）。

---

## 核心模块 (`core`)
提供全局功能支持。

### 子模块
- **`di`**  
  全局依赖注入模块。

- **`utils`**  
  通用工具类模块。

---

## 业务逻辑层 (`domain`)
专注于业务逻辑，独立于 UI。

### 子模块
- **`model`**  
  业务层特有的数据模型，与 UI 层数据模型解耦。

- **`usecase`**  
  用例模块，包含具体的业务逻辑实现。

---

## 导航模块 (`navigation`)
处理页面导航逻辑。

### 子模块
- **`NavDestination.kt`**  
  定义导航目标。
- **`NavGraph.kt`**  
  定义应用的导航图。

---

## UI 层 (`ui`)
负责用户界面的展示与交互。

### 子模块
- **`components`**  
  通用 UI 组件模块：
  - `BottomNavBar.kt`：底部导航栏组件。

- **`screens`**  
  独立页面模块：
  - `HomeScreen.kt`：主页面。
  - `DiscoverScreen.kt`：发现页面。
  - `MeScreen.kt`：个人页面。

- **`theme`**  
  全局主题样式模块：
  - `Color.kt`：自定义颜色。
  - `Theme.kt`：全局主题管理。
  - `Type.kt`：字体样式定义。

- **`MainActivity.kt`**  
  包含导航逻辑的主页面。

---

## ViewModel 层 (`viewmodel`)
负责将数据和逻辑绑定到 UI。

### 子模块
- **`DiscoverViewModel.kt`**  
  发现页面的 ViewModel。
- **`HomeViewModel.kt`**  
  主页面的 ViewModel。
- **`MeViewModel.kt`**  
  个人页面的 ViewModel。
