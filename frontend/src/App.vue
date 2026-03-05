<template>
  <!-- 
    应用根组件
    router-view 是路由出口，所有页面组件会渲染到这里
  -->
  <div id="app">
    <router-view />
  </div>
</template>

<script setup>
/**
 * App.vue - 应用根组件
 * 
 * 这是整个应用的入口组件，负责：
 * 1. 提供应用容器
 * 2. 通过 router-view 渲染路由匹配的页面组件
 * 3. 定义全局样式
 */
// 使用 Composition API，无需额外逻辑
</script>

<style>
/**
 * 全局样式
 * 应用于整个应用的容器
 */
/* 应用容器样式 */
#app {
  min-height: 100vh;  /* 最小高度为视口高度 */
  display: flex;      /* 弹性布局 */
  flex-direction: column;  /* 垂直排列 */
}
</style>