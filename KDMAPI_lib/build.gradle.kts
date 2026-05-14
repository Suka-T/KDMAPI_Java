plugins {
    // 既存のプラグイン
    `java-library`
    // 配布用JAR作成のために追加
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
}

dependencies {
    // 既存の依存関係（api を維持）
    api("net.java.dev.jna:jna:5.13.0")
    api("net.java.dev.jna:jna-platform:5.13.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks {
    // --- ここから追加：配布用の設定 ---

    // shadowJarタスクの設定
    shadowJar {
        // 出力されるファイル名
        archiveBaseName.set("kdmapi")
        archiveClassifier.set("")
        
        // ライブラリとして使う場合、このJAR一つで完結するように設定
        mergeServiceFiles()
    }

    // リソースフォルダ（DLL）をJARに含める
    processResources {
    	// 重複が発生した場合、新しい方のファイルを優先して上書きする設定を追加
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        
        from("src/main/resources") {
            include("**/*.dll")
        }
    }
}