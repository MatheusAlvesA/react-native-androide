import React, { Component } from 'react';
import { requireNativeComponent } from 'react-native';

const BannerView = requireNativeComponent('BannerViewManager');

export default class AdMobBanner extends Component {

  constructor(props) {
    super(props);

    this.state = {
      width: 0,
      height: 0
    }
  }

  handleSizeChange = dimensions => {
    const { width, height } = dimensions.nativeEvent;
    this.setState({ width, height });
    if (this.props.onSizeChange) {
      this.props.onSizeChange({ width, height });
    }
  }

  handleFailedToLoad = error => {
    if (this.props.onFailedToLoad) {
      this.props.onFailedToLoad(error.nativeEvent.message);
    }
  }

  handleOnLoad = () => {
    if (this.props.onLoad) {
      this.props.onLoad();
    }
  }

  handleOnOpen = () => {
    if (this.props.onOpen) {
      this.props.onOpen();
    }
  }

  handleOnClick= () => {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }

  handleOnClose= () => {
    if (this.props.onClose) {
      this.props.onClose();
    }
  }

  render() {
    return (
      <BannerView
        style={[
          this.props.style,
          {
            width: this.state.width,
            height: this.state.height
          }
        ]}
        onSizeChange={this.handleSizeChange}
        onFailedToLoad={this.handleFailedToLoad}
        onLoad={this.handleOnLoad}
        onOpen={this.handleOnOpen}
        onClick={this.handleOnClick}
        onClose={this.handleOnClose}
      />
    );
  }

}
