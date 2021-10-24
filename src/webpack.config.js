const path = require('path');

module.exports = {
    devtool: 'source-map',
    entry: path.resolve(__dirname, 'src/main/react/TemperatureConversionGradingApp.jsx'),
    mode: 'development',
    output: {
        path: path.resolve(__dirname, 'src/main/resources/static/dist'),
        filename: 'TemperatureConversionGradingApp.js',
        hashFunction: 'sha256'
    },
    module: {
        rules: [{
            test: /\.(js|jsx)$/,
            exclude: /node_modules/,
            loader: 'babel-loader',
            options: {
                presets: [
                    '@babel/preset-env',
                    '@babel/preset-react'
                ]
            }
        }]
    },
    resolve: {
        extensions: ['.js', '.jsx']
    }
};