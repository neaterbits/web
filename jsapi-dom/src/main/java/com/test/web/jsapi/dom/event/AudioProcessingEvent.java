package com.test.web.jsapi.dom.event;

import com.test.web.document.html.common.enums.HTMLEvent;
import com.test.web.jsapi.audio.AudioBuffer;
import com.test.web.jsapi.dom.Element;
import com.test.web.jsapi.dom.Event;

@Deprecated
public final class AudioProcessingEvent<ELEMENT> extends Event<ELEMENT> {

    private final double playbackTime;
    private final AudioBuffer audioBuffer;
    
    public AudioProcessingEvent(Element<ELEMENT, ?, ?, ?> target, long timeStamp, boolean composed, boolean isTrusted,
            
            double playbackTime, AudioBuffer audioBuffer) {
        super(target, HTMLEvent.AUDIOPROCESS, timeStamp, composed, isTrusted);

        this.playbackTime = playbackTime;
        this.audioBuffer = audioBuffer;
    }

    public double getPlaybackTime() {
        return playbackTime;
    }

    public AudioBuffer getAudioBuffer() {
        return audioBuffer;
    }
}
