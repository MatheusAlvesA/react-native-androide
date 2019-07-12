import React, { Component } from 'react';
import { requireNativeComponent, View } from 'react-native';

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
          height: '100%'
        }}
        url={props.url}
      />
    </View>
  );
}

export default VideoContainer;
