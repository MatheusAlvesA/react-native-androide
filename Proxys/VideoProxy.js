import React, { Component } from 'react';
import { ProgressBarAndroid, requireNativeComponent,
         View, Image } from 'react-native';

const VideoView = requireNativeComponent('VideoViewManager');

function VideoContainer(props) {
  const styles = props.style ? props.style : {};

  return (
    <View style={{
      ...styles,
      overflow: 'hidden'
    }}>
      <VideoView style={{
          width: '100%',
          height: '100%',
          position: 'absolute',
          top: 0,
          left: 0
        }}
        url={props.url}
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
        }}
      >
        <Image
          style={{width: '10%', height: 30}}
          source={require('../res/imgs/pause_icon.png')}
          resizeMode='center'
        />
        <ProgressBarAndroid
          style={{
            width: '90%',
            height: 15
          }}
          styleAttr="Horizontal"
          color="white"
        />
      </View>
    </View>
  );
}

export default VideoContainer;
