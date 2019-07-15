import React, { Component } from 'react';
import { ProgressBarAndroid, requireNativeComponent, DeviceEventEmitter,
         View, Image, TouchableWithoutFeedback } from 'react-native';

const VideoView = requireNativeComponent('VideoViewManager');

export default class VideoContainer extends Component {

  constructor(props) {
    super(props);

    this.state = {
      progress: 0.0,
      seekTo: 0.0,
      paused: true
    };

    this.styles = props.style ? props.style : {};
    this.id = this.generateID();
    this.controlsOnScreenSince = Date.now();
    this.idCBControlsVisible = null;
    this.visibleControls = false;
    this.mounted = false;
    this.progressBarWidth = 1;
  }

  componentDidMount() {
    this.mounted = true;
    DeviceEventEmitter.addListener('RNA_videoProgress', this.progressListener);
  }

  componentWillUnmount() {
    this.mounted = false;
  }

  progressListener = data => {
    if(!this.mounted) return;

    if(data.id === this.id) {
      let progress = (data.currentPosition / data.duration);
      if(progress === 1) {
        this.reset();
      }
      this.setState({progress});
    }
  }

  touched = () => {
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
    if(!this.paused) {
      this.setState({paused: !this.state.paused});
    }
    this.setState({seekTo: 0.0});
  }

  seek = evn => {
    if(!this.visibleControls) {
      return;
    }
    this.setState({
                    seekTo: ( evn.nativeEvent.locationX /
                                this.progressBarWidth )
                  });
  }

  render() {
    return (
      <TouchableWithoutFeedback
        onPress={this.touched}
      >
        <View style={{
          ...this.styles,
          overflow: 'hidden'
        }}>
          <VideoView style={{
              width: '100%',
              height: '100%',
              position: 'absolute',
              top: 0,
              left: 0
            }}
            url={this.props.url}
            id={this.id}
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
                source={
                        this.state.paused ?
                        require('../res/imgs/play_icon.png') :
                        require('../res/imgs/pause_icon.png')
                      }
                resizeMode='center'
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

  generateID = () =>  ('_' + Math.random().toString(36).substr(2, 9));

}
