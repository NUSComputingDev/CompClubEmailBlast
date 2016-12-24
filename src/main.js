/*
 * Server Entry Point
 */

import {setRoutes} from './routes';

const express = require('express');
const app = express();

setRoutes(app);
app.listen(8888, () => { console.log('Connection Made Successfully'); });
