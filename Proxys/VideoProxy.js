import React, { Component } from 'react';
import { ProgressBarAndroid, requireNativeComponent, DeviceEventEmitter,
         View, Image, TouchableWithoutFeedback } from 'react-native';

const VideoView = requireNativeComponent('VideoViewManager');

export default class VideoContainer extends Component {

  constructor(props) {
    super(props);
    let paused = true;

    if(typeof props.autoPlay === 'boolean') {
      paused = !props.autoPlay;
    }

    this.state = {
      progress: 0.0,
      seekTo: 0.0,
      paused
    };

    this.styles = props.style ? props.style : {};
    this.id = this.generateID();
    this.controlsOnScreenSince = Date.now();
    this.idCBControlsVisible = null;
    this.visibleControls = false;
    this.mounted = false;
    this.progressBarWidth = 1;
    this.videoWidth = -1;
    this.videoHeight = -1;
    this.containerWidth = 0;
    this.containerHeight = 0;
  }

  componentDidMount() {
    this.mounted = true;
    DeviceEventEmitter.addListener('RNA_videoProgress', this.progressListener);
    DeviceEventEmitter.addListener('RNA_videoResolutionChanged', this.resolutionChangedListener);
  }

  componentWillUnmount() {
    this.mounted = false;
  }

  generateID = () =>  ('_' + Math.random().toString(36).substr(2, 9));

  progressListener = status => {
    if(!this.mounted) return;

    if(status.id === this.id) {
      let progress = (status.currentPosition / status.duration);
      if(isNaN(progress) || progress < 0 || progress > 1) {
        progress = 0;
      }
      if(status.completed) {
        this.reset();
      }
      this.setState({progress});
    }
  }

  resolutionChangedListener = res => {
    if(!this.mounted) return;

    if(res.id === this.id) {
      this.videoWidth = res.width;
      this.videoHeight = res.height;
      this.forceUpdate();
    }
  }

  touched = () => {
    if(this.props.disableControls) {
      return;
    }

    if(this.visibleControls) {
      this.visibleControls = false;
      if(this.idCBControlsVisible !== null) {
        clearTimeout(this.idCBControlsVisible);
        this.idCBControlsVisible = null;
      }
    } else {
      this.visibleControls = true;
      this.idCBControlsVisible = setTimeout(
        () => {
          this.visibleControls = false;
          this.forceUpdate();
          this.idCBControlsVisible = null;
        },
        5000
      );
    }

    this.forceUpdate();
  }

  pause = () => {
    if(!this.visibleControls) {
      return;
    }

    this.setState({paused: !this.state.paused});
  }

  reset = () => {
    this.setState({
                    paused: true,
                    seekTo: 0.0
                  });
  }

  seek = evn => {
    if(!this.visibleControls || this.props.disableSeekTo) {
      return;
    }

    this.setState({
                    seekTo: ( evn.nativeEvent.locationX /
                                this.progressBarWidth )
                  });
  }

  render() {
    let iconPlay = this.props.playIcon ?
                   this.props.playIcon :
                   require('../res/imgs/play_icon.png');

    let iconPause = this.props.pauseIcon ?
                    this.props.pauseIcon :
                    require('../res/imgs/pause_icon.png');

    let width = '100%';
    let height = '100%';

    if(this.videoHeight > 0 && this.videoWidth > 0 && !this.props.stretch) {
      if(this.videoWidth > this.videoHeight) {          // Widscreen
        width = this.containerWidth;
        height = (width/this.videoWidth)*this.videoHeight;
        if(height > this.containerHeight) {            // Incorrect proportion, recalculating
          height = this.containerHeight;
          width = (height/this.videoHeight)*this.videoWidth;
        }
      } else {                                         // Portrait
        height = this.containerHeight;
        width = (height/this.videoHeight)*this.videoWidth;
        if(width > this.containerWidth) {              // Incorrect proportion, recalculating
          width = this.containerWidth;
          height = (width/this.videoWidth)*this.videoHeight;
        }
      }
    }

    return (
      <TouchableWithoutFeedback
        onPress={this.touched}
      >
        <View
          style={{
            ...this.styles,
            overflow: 'hidden',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center'
          }}
          onLayout={evn => {
                            this.containerWidth = evn.nativeEvent.layout.width;
                            this.containerHeight = evn.nativeEvent.layout.height;
                            this.forceUpdate();
                          }}
        >
          <VideoView style={{ width, height }}
            id={this.id}
            src={this.props.src}
            paused={this.state.paused}
            progress={this.state.seekTo}
          />

          <View style={{
              display: 'flex',
              flexDirection: 'row',
              width: '100%',
              height: 50,
              position: 'absolute',
              bottom: 0,
              left: 0,
              alignItems: 'center',
              justifyContent: 'space-between',
              paddingLeft: 5,
              paddingRight: 5,
              opacity: this.visibleControls ? 1 : 0
            }}
          >
            <TouchableWithoutFeedback
              onPress={this.pause}
            >
              <Image
                style={{width: '10%', height: 30}}
                source={this.state.paused ? iconPlay : iconPause}
                resizeMode='center'
                fadeDuration={0}
              />
            </TouchableWithoutFeedback>
            <TouchableWithoutFeedback
              onPress={this.seek}
            >
              <ProgressBarAndroid
                style={{
                  width: '90%',
                  height: 15
                }}
                styleAttr="Horizontal"
                color="white"
                indeterminate={false}
                progress={this.state.progress}
                onLayout={evn => this.progressBarWidth = evn.nativeEvent.layout.width}
              />
            </TouchableWithoutFeedback>
          </View>
        </View>
      </TouchableWithoutFeedback>
    );
  }

}
