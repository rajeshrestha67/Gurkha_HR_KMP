import SwiftUI
import ComposeApp
import Firebase

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

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
