#if targetEnvironment(simulator) // To include only if in simulator
import Foundation
import UIKit
import AVFoundation

class SIMAVCapturePhoto: AVCapturePhoto {
    //@available(iOS 11.0, *)
    init(workaround _: Void = ()) { // Dummy initializer for AVCapturePhoto
        // Call a designated initializer
    }
    // Overriding Method for fileDataRepresentation
    override func fileDataRepresentation() -> Data? {
        let image: UIImage? = UIImage(named:"hr-dummy-image")
        return image?.pngData()
    }
}


extension AVCapturePhotoOutput {

    func capturePhoto(with settings: AVCapturePhotoSettings,
                      delegate: AVCapturePhotoCaptureDelegate) {
        
        let photo: SIMAVCapturePhoto = SIMAVCapturePhoto()
        DispatchQueue.main.sync { [weak self] in
            delegate.photoOutput?(self!, didFinishProcessingPhoto: photo, error: nil)
        }
    }
}
#endif
