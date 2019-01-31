package com.famstack.projectscheduler.scheduling;

import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * The Class FamstackSchedulerFactoryBean.
 */
public class FamstackSchedulerFactoryBean extends SchedulerFactoryBean {

	    /** The enable quartz tasks. */
    	private boolean enableQuartzTasks;

	    /* (non-Javadoc)
    	 * @see org.springframework.scheduling.quartz.SchedulerFactoryBean#afterPropertiesSet()
    	 */
    	@Override
	    public void afterPropertiesSet() throws Exception {
	        if (enableQuartzTasks) {
	            super.afterPropertiesSet();
	        } 
	    }

		/**
		 * Checks if is enable quartz tasks.
		 *
		 * @return true, if is enable quartz tasks
		 */
		public boolean isEnableQuartzTasks() {
			return enableQuartzTasks;
		}

		/**
		 * Sets the enable quartz tasks.
		 *
		 * @param enableQuartzTasks the new enable quartz tasks
		 */
		public void setEnableQuartzTasks(boolean enableQuartzTasks) {
			this.enableQuartzTasks = enableQuartzTasks;
		}
}
