# NumberSlider for Jetpack Compose
[![Release](https://jitpack.io/v/LeeWeeder/NumberSlider.svg)](https://jitpack.io/#LeeWeeder/NumberSlider)

A customizable number slider component for Jetpack Compose, allowing users to select numeric values within a defined range.
## Preview
<img src="https://github.com/LeeWeeder/NumberSlider/blob/ed3d2287d9f2f0150970a357bd343f705d44ebfc/assets/preview.gif" width="150">

## Features
- **Snapping**: Provides snapping to discrete values.
- **Customizable Appearance**: Easily customize the appearance of value indicators, ruler markers, and the center marker.
- **Text Field Integration**: Includes a text field for direct input and synchronization with the slider.

## Getting Started
> [!IMPORTANT]
> The minimum required SDK for this library is set to 34 as I am using this, for now, for my personal project. If you would like this library to be compatible with a lower SDK version, feel free to open an issue, or better yet, you can implement it yourself.
### Step 1. In your `settings.gradle.kts` file, add the JitPack repository.
```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { setUrl("https://www.jitpack.io") } // Add this line
    }
}
```
### Step 2. Add the NumberSlider dependency.
In your version catalog file (`libs.versions.toml`),
```toml
[versions]
numberSlider = "1.0.4"

[libraries]
numberslider = { group = "com.github.LeeWeeder", name = "NumberSlider", version.ref = "numberSlider"}
```
In your module `build.gradle`,
```kotlin
dependencies {
    implementation(libs.numberslider)
}
```
Sync your project.
## Usage
```kotlin
var selectedValue by remember { mutableStateOf(50f) }

NumberSlider(
    value = selectedValue,
    minValue = 0, maxValue = 100,
    step = 0.5f,
    onValueChange = { newValue ->
        selectedValue = newValue
    }
)
```
## Customization
### Indicator
Customize the appearance of value indicators using the IndicatorProperties class:
```kotlin
val indicatorProperties = IndicatorProperties(
    defaultStyle = MaterialTheme.typography.bodyLarge,
    selectedStyle = MaterialTheme.typography.headlineMedium.copy(color = Color.Red)
)

NumberSlider(
    // ... other parameters
    indicatorProperties = indicatorProperties
)
```
### Ruler markers
Provide a custom composable function for ruler markers:
```kotlin
NumberSlider(
    // ... other parameters
    rulerMarker = {
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(20.dp)
                .background(Color.Gray)
        )
    }
)
```
### Center marker
Customize the center marker with a composable function:
```kotlin
NumberSlider(
    // ... other parameters
    centerMarker = {
        Icon(
            imageVector =Icons.Filled.ArrowDropDown,
            contentDescription = null,
            tint = Color.Black
        )
    }
)
```
## Documentation
For detailed API documentation, refer to the KDoc comments in the source code.
## Contributing
Contributions are welcome! Feel free to open issues or submit pull requests to improve this library.
## License
This library is licensed under the GNU GPLv3.0 license.
