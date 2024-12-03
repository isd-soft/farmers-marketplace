<template>
  <div class="messaging-page">
    <!-- Conversations Sidebar -->
    <div class="conversations-sidebar">
      <h2>Conversations</h2>
      <ul class="conversation-list">
        <li
          v-for="conversation in conversations"
          :key="conversation.id"
          :class="{ 'selected-conversation': selectedConversation?.id === conversation.id }"
          @click="selectConversation(conversation)"
        >
          <Card class="conversation-card">
            <template #title>
              {{ conversation.farmer.firstName }} {{ conversation.farmer.lastName }}
            </template>
            <template #content>
              <p>{{ conversation.lastMessage || 'No messages yet' }}</p>
            </template>
          </Card>
        </li>
      </ul>
    </div>

    <!-- Messages Section -->
    <div class="messages-section" v-if="selectedConversation">
      <h2>Chat with {{ selectedConversation.farmer.id == this.userId ? selectedConversation.customer.firstName : selectedConversation.farmer.firstName }}</h2>
      <div ref="messageList" class="messages-list" @scroll="handleScroll">
        <ul>
          <li
            v-for="message in messages.slice().reverse()"
            :key="message.id"
            :class="[
              { 'sent-message': message.sender.id == this.userId },
              { 'received-message': message.sender.id != this.userId },
              { 'farmer-message': message.sender.isFarmer },
              { 'non-farmer-message': !message.sender.isFarmer }
            ]"
          >
            <div class="message-bubble">
              <strong>{{ message.sender.firstName }}:</strong> {{ message.content }}
            </div>
          </li>
        </ul>
      </div>
      <div class="message-input-container">
        <InputText
          v-model="newMessage"
          placeholder="Type a message..."
          class="message-input"
          @keyup.enter="sendMessage"
        />
        <Button
          icon="pi pi-send"
          class="p-button-rounded p-button-primary send-button"
          @click="sendMessage"
        />
      </div>
    </div>

    <!-- Placeholder if no conversation is selected -->
    <div class="messages-section no-conversation-selected" v-else>
      <p>Please select a conversation to start chatting.</p>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, watch } from 'vue';
import { Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import axiosInstance, { getUserId } from '@/utils/axiosInstance.js';
import Card from 'primevue/card';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';

export default {
  name: 'MessageList',
  components: {
    Card,
    Button,
    InputText,
  },
  setup() {
    const conversations = ref([]);
    const selectedConversation = ref(null);
    const messages = ref([]);
    const newMessage = ref('');
    const stompClient = ref(null);
    const page = ref(0);
    const size = ref(50);
    const loadingMessages = ref(false);

    const fetchConversations = async () => {
      const response = await axiosInstance.get('/messaging/conversations', {
        params: { page: page.value, size: size.value },
      });
      conversations.value = response.data;
    };

    const sendMessage = async () => {
      if (!selectedConversation.value || !newMessage.value.trim()) return;
      await axiosInstance.post('/messaging/conversations', {
        conversationId: selectedConversation.value.id,
        content: newMessage.value.trim(),
      });
      newMessage.value = '';
    };

    const connectWebSocket = () => {
      if (stompClient.value) {
        stompClient.value.disconnect();
      }

      const socket = new SockJS('http://localhost:8080/ws');
      stompClient.value = Stomp.over(socket);
      stompClient.value.connect({}, () => {
        if (selectedConversation.value) {
          stompClient.value.subscribe(`/topic/messages/${selectedConversation.value.id}`, (message) => {
            const receivedMessage = JSON.parse(message.body);
            messages.value.unshift(receivedMessage);
          });
        }
      });
    };

    const selectConversation = async (conversation) => {
      selectedConversation.value = conversation;
      page.value = 0;

      const response = await axiosInstance.get(`/messaging/conversations/${conversation.id}`, {
        params: { page: page.value, size: size.value, sort: 'sentAt,desc' },
      });
      messages.value = response.data;

      connectWebSocket();
    };

    const loadMoreMessages = async () => {
      if (loadingMessages.value || page.value === null) return;
      page.value++;
      loadingMessages.value = true;
      const response = await axiosInstance.get(`/messaging/conversations/${selectedConversation.value.id}`, {
        params: { page: page.value, size: size.value, sort: 'sentAt,desc' },
      });

      if (response.data.length > 0) {
        messages.value = [...messages.value, ...response.data];
      } else {
        page.value = null;
      }
      loadingMessages.value = false;
    };

    const handleScroll = () => {
      const messageList = document.querySelector('.messages-list');
      if (messageList.scrollTop === 0) {
        loadMoreMessages();
      }
    };

    onMounted(() => {
      fetchConversations();
    });

    return {
      conversations,
      selectedConversation,
      messages,
      newMessage,
      selectConversation,
      sendMessage,
      handleScroll,
    };
  },
  data() {
    return {
      userId: null,
    };
  },
  created() {
    this.userId = getUserId();
    console.log(this.userId);
  },
};
</script>
<style scoped>
.messaging-page {
  display: flex;
  height: 100vh;
  background-color: #f4f4f4;
}

.conversations-sidebar {
  width: 25%;
  background-color: #ffffff;
  border-right: 1px solid #ddd;
  padding: 1rem;
  overflow-y: auto;
}

.conversation-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.conversation-card {
  margin-bottom: 1rem;
  cursor: pointer;
}

.selected-conversation .conversation-card {
  background-color: #e0f7fa;
}

.messages-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 1rem;
  background-color: #ffffff;
}

.no-conversation-selected {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 1.2rem;
  color: #888;
}

.messages-list {
  flex: 1;
  overflow-y: auto;
  margin-bottom: 1rem;
}

.messages-list ul {
  list-style: none;
  padding: 0;
}

.message-bubble {
  padding: 0.8rem;
  border-radius: 8px;
  margin-bottom: 0.5rem;
  max-width: 70%;
}
.sent-message {
  display: flex;
  justify-content: flex-end;  /* Align the entire message to the right */
}

.received-message {
  display: flex;
  justify-content: flex-start;  /* Align the entire message to the left */
}
.farmer-message .message-bubble {
  background-color: green !important;
  color: white;
}

.non-farmer-message .message-bubble {
  background-color: yellow !important;
  color: black;
}

.message-input-container {
  display: flex;
  align-items: center;
}

.message-input {
  flex: 1;
  margin-right: 0.5rem;
}

.send-button {
  flex-shrink: 0;
}
</style>
