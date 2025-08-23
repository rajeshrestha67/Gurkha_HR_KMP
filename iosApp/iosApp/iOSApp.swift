import SwiftUI
import ComposeApp

@main
struct iOSApp: App {


    init(){
        IOSKoinInitKt.iOSKoinInit()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}