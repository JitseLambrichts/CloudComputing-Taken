<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AnalyticsController;

Route::get('/', function () {
    return view('welcome');
});

Route::get('/analytics', [AnalyticsController::class, 'getStatus']);

Route::get('/api/players', [AnalyticsController::class, 'getPlayers']);
Route::get('/analytics', [AnalyticsController::class, 'getStatus']);
