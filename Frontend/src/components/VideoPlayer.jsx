import videojs from 'video.js'
import 'video.js/dist/video-js.css'
import Hls from 'hls.js'
import { useEffect, useRef } from 'react'

export default function VideoPlayer({ src }) {
  const videoRef = useRef(null)
  const playerRef = useRef(null)

  useEffect(() => {
    const videoElement = videoRef.current

    // Add slight delay to ensure DOM is ready
    setTimeout(() => {
      playerRef.current = videojs(videoElement, {
        controls: true,
        fluid: false,
        preload: 'auto',
        muted: true,       // important for autoplay
        autoplay: false     // chrome allows muted autoplay
      })

      // HLS.js for Chrome/Firefox/Edge
      if (Hls.isSupported()) {
        const hls = new Hls()
        hls.loadSource(src)
        hls.attachMedia(videoElement)
      }

      // Native HLS (Safari, iPhone)
      else if (videoElement.canPlayType('application/vnd.apple.mpegurl')) {
        videoElement.src = src
      }
    }, 50)

    return () => {
      if (playerRef.current) {
        playerRef.current.dispose()
      }
    }
  }, [src])

  return (
    <div>
      <div data-vjs-player>
        <video ref={videoRef} className="video-js vjs-big-play-centered" />
      </div>
    </div>
  )
}
