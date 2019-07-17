import React, { Component } from 'react';
import { requireNativeComponent } from 'react-native';

const BannerView = requireNativeComponent('BannerViewManager');

export default class AdMobBanner extends Component {

  constructor(props) {
    super(props);
    //TODO
  }

  componentDidMount() {
    this.mounted = true;
  }

  componentWillUnmount() {
    this.mounted = false;
  }

  render() {
    return (
      <BannerView />
    );
  }

}
