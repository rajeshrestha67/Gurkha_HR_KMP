import SwiftUI
import ComposeApp

@main
struct iOSApp: App {


    init() {
        IOSKoinInitKt.iOSKoinInit()
        SetupLoggerKt.setupLogger(platform: "iOS")
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }

}
