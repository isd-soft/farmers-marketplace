<template>
  <div class="server-info-page">
    <h1 class="page-title">Server Information</h1>

    <!-- Server Info Section -->
    <div class="server-info" v-if="serverInfo">
      <h2 class="section-title">General Information</h2>
      <div class="info-card">
        <div v-for="(value, key) in serverInfo" :key="key" class="info-item">
          <span class="info-key">{{ key }}:</span>
          <span class="info-value">
            <template v-if="isObjectOrArray(value)">
              <div v-if="Array.isArray(value)">
                <ul class="value-list">
                  <li v-for="(item, index) in value" :key="index">{{ item }}</li>
                </ul>
              </div>
              <div v-else>
                <div v-for="(subValue, subKey) in value" :key="subKey" class="nested-item">
                  <span class="info-subkey">{{ subKey }}:</span>
                  <span class="info-subvalue">{{ subValue }}</span>
                </div>
              </div>
            </template>
            <template v-else>
              {{ value }}
            </template>
          </span>
        </div>
      </div>
    </div>

    <!-- Requests Info Section -->
    <div class="requests-info">
      <h2 class="section-title">Number of Requests statistics</h2>
      <Dropdown
        v-model="selectedInterval"
        :options="timeIntervals"
        optionLabel="label"
        class="p-mb-3"
        placeholder="Select Time Interval"
        @change="fetchRequestsInfo"
      />
      <table v-if="requestsInfo" class="info-table">
        <thead>
        <tr>
          <th>Handler</th>
          <th>Number of Requests</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(value, key) in requestsInfo" :key="key">
          <td>{{ key }}</td>
          <td>
            <!-- Handle nested arrays or objects for requestsInfo -->
            <template v-if="isObjectOrArray(value)">
              <div v-if="Array.isArray(value)">
                <ul class="value-list">
                  <li v-for="(item, index) in value" :key="index">{{ item }}</li>
                </ul>
              </div>
              <div v-else>
                <div v-for="(subValue, subKey) in value" :key="subKey" class="nested-item">
                  <span class="info-subkey">{{ subKey }}:</span> {{ subValue }}
                </div>
              </div>
            </template>
            <template v-else>
              {{ value }}
            </template>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import Dropdown from 'primevue/dropdown';
import axiosInstance from '@/utils/axiosInstance.js';

export default {
  components: { Dropdown },
  setup() {
    const serverInfo = ref(null);
    const requestsInfo = ref(null);
    const selectedInterval = ref(null);

    // Time intervals matching the enum in the backend
    const timeIntervals = ref([
      { label: '1 Hour', value: 'HOURS_1' },
      { label: '1 Minute', value: 'MINUTES_1' },
      { label: '5 Minutes', value: 'MINUTES_5' },
      { label: '5 Seconds', value: 'SECONDS_5' },
      { label: '24 Hours', value: 'HOURS_24' },
      { label: 'Reset Interval', value: 'RESET_INTERVAL' },
    ]);

    const fetchServerInfo = async () => {
      try {
        const response = await axiosInstance.get('/server-info');
        serverInfo.value = response.data;
      } catch (error) {
        console.error('Error fetching server info:', error);
      }
    };

    const fetchRequestsInfo = async () => {
      if (!selectedInterval.value) {
        return;
      }
      try {
        const response = await axiosInstance.get(`/server-info/requests`, {
          params: { interval: selectedInterval.value.value },
        });
        requestsInfo.value = response.data;
      } catch (error) {
        console.error('Error fetching requests info:', error);
      }
    };

    const isObjectOrArray = (value) => {
      return value && (typeof value === 'object' || Array.isArray(value));
    };

    onMounted(() => {
      fetchServerInfo();
    });

    return {
      serverInfo,
      requestsInfo,
      selectedInterval,
      timeIntervals,
      fetchRequestsInfo,
      isObjectOrArray,
    };
  },
};
</script>

<style scoped>
.server-info-page {
  padding: 2rem;
  font-family: Arial, sans-serif;
  color: #333;
  background-color: #f4f4f9;
  width: 70%;
  min-width: 70%;
  margin: 0 auto;
}

.page-title {
  font-size: 2.5rem;
  font-weight: bold;
  margin-bottom: 1.5rem;
  text-align: center;
  color: #444;
}

.section-title {
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 1.5rem;
  color: #444;
}

.info-card {
  background-color: #fff;
  padding: 2rem;
  border-radius: 8px;
  border: 1px solid #ddd;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  margin-bottom: 2rem;
  width: 100%;
}

.info-item {
  display: flex;
  flex-wrap: wrap; /* Allows wrapping when necessary */
  justify-content: space-between;
  margin: 1.2rem 0;
  font-size: 1.2rem;
}

.info-key {
  font-weight: bold;
  color: #555;
  min-width: 150px; /* Prevents the key from being too narrow */
}

.info-value {
  color: #777;
  font-size: 1.2rem;
  flex-grow: 1; /* Makes the value flexible and prevent it from pushing too far right */
  padding-left: 15px; /* Ensures some spacing between key and value */
}

.value-list {
  padding-left: 0;
  margin: 0;
  font-size: 1.2rem;
}

.value-list li {
  padding-left: 15px; /* Adjust padding to align list items correctly */
}

.nested-item {
  margin: 1rem 0;
}

.info-subkey {
  font-weight: bold;
  color: #444;
}

.info-subvalue {
  color: #666;
}

.requests-info {
  margin-top: 2.5rem;
}

.info-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1.5rem;
  background-color: #fff;
  font-size: 0.9rem;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.info-table th,
.info-table td {
  border: 1px solid #ddd;
  padding: 1.2rem;
  text-align: left;
}

.info-table th {
  background-color: #f4f4f4;
  font-weight: bold;
}

.info-table tbody tr:nth-child(even) {
  background-color: #fafafa;
}

.info-table tbody tr:hover {
  background-color: #f1f1f1;
  cursor: pointer;
}

.info-table td {
  word-wrap: break-word;
  max-width: 350px;
  padding-right: 10px;
}

@media (max-width: 1200px) {
  .server-info-page {
    padding: 1.5rem;
  }

  .page-title {
    font-size: 1.8rem;
  }

  .section-title {
    font-size: 1.5rem;
  }

  .info-item {
    font-size: 1rem;
  }

  .info-key,
  .info-value {
    font-size: 1.1rem;
  }

  .info-table th,
  .info-table td {
    padding: 1rem;
  }
}

@media (max-width: 768px) {
  .server-info-page {
    padding: 1.2rem;
    width: 90%;
  }

  .page-title {
    font-size: 1.8rem;
  }

  .section-title {
    font-size: 1.5rem;
  }

  .info-card {
    padding: 1.5rem;
  }

  .info-item {
    font-size: 0.9rem;
    flex-direction: column;
  }

  .info-key {
    margin-bottom: 0.5rem;
  }

  .info-value {
    margin-bottom: 1rem;
  }

  .info-table th,
  .info-table td {
    padding: 0.8rem;
    font-size: 1rem;
  }
}

</style>
