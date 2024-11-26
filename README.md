# 项目结构

## `app/`
Android 项目文件夹，包含应用的所有源代码和资源。

### `build.gradle.kts`
- 项目级 `build.gradle` 文件，包含依赖配置等。

### `src/main/`
项目的主代码目录。

#### `AndroidManifest.xml`
- 应用的 Android 清单文件，包含基本的配置信息和权限声明。

#### `java/com/lyy/myapp/`
应用的 Java/Kotlin 源代码文件夹。

##### `di/`
依赖注入模块，负责应用中依赖关系的管理。

- **`AppModule.kt`**: 依赖注入模块配置文件。

##### `domain/`
领域层，包含业务逻辑和领域模型。

- **`model/`**: 包含领域模型类。

  - **`User.kt`**: 领域模型类，表示用户对象。

  - **`UserRepository.kt`**: 定义用户相关的业务逻辑接口。

##### `data/`
数据层，包含与数据交互的逻辑，负责获取、存储数据。

- **`local/`**: 本地数据源，涉及数据库操作。

  - **`UserDao.kt`**: 用于用户数据的本地数据库操作（如 Room）。

- **`network/`**: 网络数据源，负责 API 请求。

  - **`ApiService.kt`**: 定义网络请求接口，使用 Retrofit 之类的库进行 API 调用。

- **`repository/`**: 数据仓库，连接本地和远程数据源。

  - **`UserRepositoryImpl.kt`**: 数据仓库的实现类，处理用户相关的数据操作。

- **`remote/`**: 远程数据源，负责处理网络请求的结果。

  - **`RemoteDataSource.kt`**: 远程数据源类，定义了从远程服务器获取数据的逻辑。

##### `ui/`
用户界面层，包含 UI 组件和屏幕（Screen）。

- **`screens/`**: UI 屏幕文件夹，包含应用各个界面的实现。

  - **`HomeScreen.kt`**: 主界面，用 Jetpack Compose 实现。

  - **`LoginScreen.kt`**: 登录界面。

- **`components/`**: 可复用 UI 组件。

  - **`Button.kt`**: 自定义按钮组件。

- **`theme/`**: 主题文件夹，定义应用的视觉样式。

  - **`Color.kt`**: 自定义颜色。

  - **`Shapes.kt`**: 自定义形状。

  - **`Typography.kt`**: 字体样式设置。

##### `viewmodel/`
视图模型层，负责与 UI 层进行数据交互，处理业务逻辑。

- **`HomeViewModel.kt`**: 主界面相关的 ViewModel。

- **`LoginViewModel.kt`**: 登录界面相关的 ViewModel。

##### `navigation/`
导航层，负责应用内的页面导航。

- **`NavGraph.kt`**: 定义 Jetpack Compose 导航图，管理各个屏幕之间的跳转。

##### `utils/`
工具类层，包含应用中的扩展函数和其他辅助功能。

- **`Extensions.kt`**: 扩展函数，提供常用的工具方法。

##### `MainActivity.kt`
应用的入口 Activity，通常用于启动应用并加载第一个屏幕。

#### `res/`
资源文件夹，包含所有的资源文件（如布局、图像等）。

- **`layout/`**: 布局文件，通常是传统的 XML 布局文件。

- **`drawable/`**: 图形资源文件，如图片等。

- **`values/`**: 值资源文件，如颜色、字符串、尺寸等。

---

## 根目录
- `build.gradle.kts`: 根目录的 `build.gradle` 配置文件，包含全局配置和依赖管理。
