<template>
  <div>
    <FullCalendar class="demo-app-calendar" :options="calendarOptions">
      <template #eventContent="arg">
        <b>{{ arg.timeText }}</b>
        <i>{{ arg.event.title }}</i>
      </template>
    </FullCalendar>
  </div>
</template>
<script>
import { defineComponent } from 'vue'
import '@fullcalendar/core/vdom' // solve problem with Vite
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin from '@fullcalendar/interaction'
import { ElMessageBox, ElMessage } from 'element-plus'
import { INITIAL_EVENTS, createEventId } from './event-utils'
import axios from 'axios'

export default defineComponent({
  name: 'Calendar',
  components: {
    FullCalendar
  },
  data() {
    return {
      userinfo: JSON.parse(localStorage.getItem('userInfo')),
      calendarOptions: {
        plugins: [
          dayGridPlugin,
          timeGridPlugin,
          interactionPlugin // needed for dateClick
        ],
        headerToolbar: {
          left: 'prev,next today',
          center: 'title',
          right: 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        initialView: 'dayGridMonth',
        // initialEvents: [], // 可选项，可以从远程接口返回初始化数据
        events: [],
        editable: true,
        selectable: true,
        selectMirror: true,
        dayMaxEvents: true,
        weekends: true,
        select: this.handleDateSelect,
        eventClick: this.handleEventClick,
        eventsSet: this.handleEvents,
        eventAdd: this.addEvent,
        eventChange: this.eventChange,
        eventRemove: this.eventRemove
      },
      currentEvents: [],
    }
  },
  // created() {
  //   console.log('----------------')
  //   axios.get(`/api/task/find/${this.userinfo.userName}`).then(resp=>{
  //     console.log(resp.data)
  //     this.calendarOptions.events=[]
  //     for (let i=0;i<resp.data.length;i++){
  //       const d = new Date(resp.data[i].date)
  //       // console.log(d)
  //       const startStr = resp.data[i].allDay ? (`${d.getFullYear()  }-${  (`0${d.getMonth()+1}`).slice(-2)  }-${     (`0${  d.getDate()}`).slice(-2)}`) :  (`${d.getFullYear()  }-${  (`0${d.getMonth()+1}`).slice(-2)  }-${ (`0${  d.getDate()}`).slice(-2)  }T${  (`0${  d.getHours()}`).slice(-2)  }:${ (`0${  d.getMinutes()}`).slice(-2)  }:${  (`0${  d.getSeconds()}`).slice(-2)}`)
  //       this.calendarOptions.events.push({id:(resp.data[i].id-1).toString(),title:resp.data[i].title,start:startStr})
  //     }
  //   })
  //   console.log(this.calendarOptions.events)
  // },
  methods: {
    handleWeekendsToggle() {
      this.calendarOptions.weekends = !this.calendarOptions.weekends // update a property
    },
    /**
     * @description 选中某天处理事件
     */
    // handleDateSelect(selectInfo) {
    //   // eslint-disable-next-line no-alert
    //   const title = prompt('请输入任务标题')
    //   const calendarApi = selectInfo.view.calendar
    //
    //   calendarApi.unselect() // clear date selection
    //
    //   if (title) {
    //     calendarApi.addEvent({
    //       id: createEventId(),
    //       title,
    //       start: selectInfo.startStr,
    //       end: selectInfo.endStr,
    //       allDay: selectInfo.allDay
    //     })
    //   }
    // },
    /**
     * @description 选中当前任务事件
     */
    // handleEventClick(clickInfo) {
    //   ElMessageBox.confirm('确定删除当前任务吗？', '温馨提示', {
    //     confirmButtonText: '确定',
    //     cancelButtonText: '取消',
    //     type: 'warning'
    //   })
    //     .then(() => {
    //       clickInfo.event.remove()
    //
    //       ElMessage({
    //         type: 'success',
    //         message: '删除成功'
    //       })
    //     })
    //     .catch(() => {
    //       ElMessage({
    //         type: 'info',
    //         message: '已取消删除'
    //       })
    //     })
    // },
    /**
     * @description 选中当前事件
     */
    // handleEvents(events) {
    //   this.currentEvents = events
    // },
    // addEvent(events) {
    //   console.log(events.event.title)
    //   console.log(events.event.start)
    //   const d = new Date(events.event.start)
    //   console.log(d.toLocaleDateString())
    //   // 调用后端接口
    //   console.log(this.userinfo)
    //   const form = new FormData()
    //   form.append('sid',this.userinfo.userName)
    //   form.append('title',events.event.title)
    //   form.append('date',d.getTime())
    //   console.log(d.getTime())
    //   form.append('allday',true)
    //   axios.post('/api/task/add',form).then(resp=>{
    //
    //   })
    // },
    // eventChange(events) {
    //   console.log(events)
    // },
    // eventRemove(events) {
    //   console.log(events.event.title)
    //   console.log(events.event.start)
    //   const d = new Date(events.event.start)
    //   console.log(d.toLocaleDateString())
    // }
  }
})
</script>
<style lang="stylus" scoped></style>
